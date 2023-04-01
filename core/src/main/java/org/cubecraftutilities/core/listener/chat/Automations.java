package org.cubecraftutilities.core.listener.chat;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.subconfig.EndGameSubConfig;
import org.cubecraftutilities.core.config.subconfig.EndGameSubConfig.GameEndMessage;
import org.cubecraftutilities.core.managers.CCUManager;
import org.cubecraftutilities.core.managers.submanagers.FriendTrackerManager;
import org.cubecraftutilities.core.utils.Colours;
import org.cubecraftutilities.core.utils.eggwarsmaps.OnlineFriendLocation;

public class Automations {

  private final CCU addon;
  private final CCUManager manager;

  private final Pattern playerElimination = Pattern.compile("([a-zA-Z0-9_]{2,16}) has been eliminated from the game\\.");
  private final Pattern EggWarsTeamJoin = Pattern.compile("You have joined .{1,30} team\\.");
  private final Pattern WhereAmIOutPut = Pattern.compile("You are on proxy: (\\w{0,2}bungeecord\\d{1,3})\\nYou are on server: (.{5})");
  private final Pattern FriendList = Pattern.compile("------- Friends \\(\\d{1,10}\\/\\d{1,10}\\) -------\n([a-zA-Z0-9_]{2,16} - .{0,200}\n?)*Offline:\n([a-zA-Z0-9_]{2,16},? ?)*");
  private final Pattern onlineFriends = Pattern.compile("\n(?<username>[a-zA-Z0-9_]{2,16}) - (?:Playing|Online on)(?: Team| Main)? (?<game>[a-zA-Z ]*?) (?:in|#\\d{1,2}) (?:map|\\[[A-Z]{2}\\]) ?(?<map>[a-zA-Z]*)?");
  private final Pattern fiveSecondsRemaining = Pattern.compile("[a-zA-Z ]{0,30} is starting in 5 seconds\\.");

  private boolean passedOffline = false;
  private boolean voted = false;

  public Automations(CCU addon) {
    this.addon = addon;
    this.manager = addon.getManager();
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent e) {
    Minecraft minecraft = this.addon.labyAPI().minecraft();
    String msg = e.chatMessage().getPlainText();
    ClientPlayer p = minecraft.getClientPlayer();
    if (p == null) {
      return;
    }

    // Friend Message Sound
    if (this.addon.configuration().getAutomationConfig().friendMessageSound().get()) {
      if (msg.matches("\\[Friend\\] ([a-zA-Z0-9_]{2,16}) -> Me: .*")) {
        ResourceLocation resourceLocation = ResourceLocation.create("minecraft", this.addon.configuration().getAutomationConfig().getFriendMessageSoundId().get());
        minecraft.sounds().playSound(resourceLocation, 100, 1);
        return;
      }
    }

    // 5 seconds remaining
    if (this.fiveSecondsRemaining.matcher(msg).matches()) {
      if (!this.voted && this.addon.configuration().getQolConfig().getReminderToVote().get()) {
        ResourceLocation resourceLocation = ResourceLocation.create("minecraft", this.addon.configuration().getQolConfig().getReminderToVoteSoundId().get());
        minecraft.sounds().playSound(resourceLocation, 100, 1);
        minecraft.chatExecutor().displayClientMessage(Component.text("Don't forget to vote!", Colours.Primary));
      }
      this.voted = false;
      return;
    }

    // Start of game
    if (msg.equals("Let the games begin!")) {
      this.manager.setInPreLobby(false);
      this.manager.setGameStartTime(System.currentTimeMillis());
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).schedule(() -> {
        if (this.addon.configuration().getEggWarsMapInfoSubConfig().isEnabled().get()) {
          this.manager.updateTeamColour();
          this.manager.getEggWarsMapInfoManager().doEggWarsMapLayout();
        }
        this.addon.rpcManager.startOfGame();
        this.addon.rpcManager.updateRPC();
      },1000, TimeUnit.MILLISECONDS);
      return;
    }

    // RPC
    Matcher matcher = playerElimination.matcher(msg);
    if (matcher.matches()) {
      this.addon.rpcManager.registerDeath(matcher.group(1));
    }

    // Auto GG
    EndGameSubConfig config = this.addon.configuration().getAutomationConfig().getEndGameSubConfig();
    if (config.isEnabled().get() && !manager.isEliminated()) {
      String eliminationMessage = p.getName() + " has been eliminated from the game.";
      if (msg.equals("Congratulations, you win!") || (msg.equals(eliminationMessage) && config.getOnElimination().get())) {
        GameEndMessage gameEndMessage = config.getGameEndMessage().get();
        if (gameEndMessage != GameEndMessage.NONE) {
          minecraft.chatExecutor().chat(this.gameEndMessagesToReadable(gameEndMessage), false);
        }
        if (!config.getCustomMessage().isDefaultValue()) {
          minecraft.chatExecutor().chat((this.manager.getPartyManager().isPartyChat() ? "!" : "") + config.getCustomMessage().get(), false);
        }
        manager.setEliminated(true);
        return;
      }
    }

    // Spawn protection countdown
    if (msg.equals("You are now invincible for 10 seconds.")) {
      this.addon.getManager().getSpawnProtectionManager().getClientPlayerSpawnProtection().registerDeath();
      return;
    }

    // TeamColour Tracker
    Matcher teamColourMatcher = this.EggWarsTeamJoin.matcher(msg);
    if (teamColourMatcher.matches()) {
      TextColor colour = e.chatMessage().component().getChildren().get(0).getChildren().get(1).getColor();
      if (colour == null) {
        this.manager.setTeamColour("yellow");
      } else {
        this.manager.setTeamColour(colour.toString());
      }
      return;
    }

    // Bungee & serverid matcher
    Matcher whereAmIMatcher = this.WhereAmIOutPut.matcher(msg);
    if (whereAmIMatcher.matches()) {
      this.manager.setBungeecord(whereAmIMatcher.group(1));
      this.manager.setServerID(whereAmIMatcher.group(2));
      return;
    }

    // Vote warn
    String voteMessage = p.getName() + " voted for .*\\. \\d{1,4} votes?";
    if (msg.matches(voteMessage) && this.manager.isInPreLobby()) {
      this.voted = true;
      return;
    }

    // Friends list shorter && tracker
    if (this.FriendList.matcher(msg).matches()) {

      if (this.addon.configuration().getQolConfig().getShortFriendsList().get()) {
        if (this.manager.hasRequestedFullFriendsList()) {
          this.manager.setRequestedFullFriendsList(false);
        } else  {
          Component shorterFriendsList = Component.empty();
          this.passedOffline = false;
          shortenFriendsList(shorterFriendsList, e.message());

          e.setMessage(shorterFriendsList);
        }
      }

      FriendTrackerManager friendTrackerManager = this.manager.getFriendTrackerManager();
      if (friendTrackerManager.isUpdating()) {
        Matcher onlineFriends = this.onlineFriends.matcher(msg);

        while (onlineFriends.find()) {
          friendTrackerManager.updateFriendLocation(
              new OnlineFriendLocation(
                  onlineFriends.group("username"),
                  onlineFriends.group("game"),
                  onlineFriends.group("map")));
        }
        friendTrackerManager.setUpdating(false);
        e.setCancelled(true);
      }
    }
  }

  private void shortenFriendsList(Component short_c, Component long_c) {
    for (Component child : long_c.getChildren()) {
      TextComponent textComponent = (TextComponent) child;
      if (textComponent.getText().contains("Offline")) {
        this.passedOffline = true;
      }
      if (!this.passedOffline) {
        short_c.append(child);
        shortenFriendsList(short_c, child);
      }
    }
  }

  private String gameEndMessagesToReadable(GameEndMessage gameEndMessage) {
    switch (gameEndMessage) {
      case GG:
        return "gg";
      case WP:
        return "wp";
      case GOOD_GAME:
        return "Good game";
      case WELL_PLAYED:
        return "Well played";
      case NONE:
        break;
    }
    return "";
  }
}
