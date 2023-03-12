package org.cubecraftutilities.core.listener.chat;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.subconfig.EndGameSubConfig;
import org.cubecraftutilities.core.managers.CCUManager;
import org.cubecraftutilities.core.managers.submanagers.FriendTrackerManager;
import org.cubecraftutilities.core.utils.eggwarsmaps.OnlineFriendLocation;

public class Automations {

  private final CCU addon;
  private final CCUManager manager;

  private final Pattern playerElimination = Pattern.compile("([a-zA-Z0-9_]{2,16}) has been eliminated from the game\\.");
  private final Pattern EggWarsTeamJoin = Pattern.compile("You have joined .{1,30} team\\.");
  private final Pattern WhereAmIOutPut = Pattern.compile("You are on proxy: (\\w{0,2}bungeecord\\d{1,3})\\nYou are on server: (.{5})");
  private final Pattern FriendList = Pattern.compile("------- Friends \\(\\d{1,10}\\/\\d{1,10}\\) -------\n([a-zA-Z0-9_]{2,16} - .{0,200}\n?)*Offline:\n([a-zA-Z0-9_]{2,16},? ?)*");
  private final Pattern onlineFriends = Pattern.compile("\n(?<username>[a-zA-Z0-9_]{2,16}) - (?:Playing|Online on)(?: Team| Main)? (?<game>[a-zA-Z ]*?) (?:in|#\\d{1,2}) (?:map|\\[[A-Z]{2}\\]) ?(?<map>[a-zA-Z]*)?");

  private boolean passedOffline = false;

  public Automations(CCU addon) {
    this.addon = addon;
    this.manager = addon.getManager();
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent e) {
    String msg = e.chatMessage().getPlainText();
    ClientPlayer p = this.addon.labyAPI().minecraft().getClientPlayer();
    if (p == null) {
      return;
    }

    // Friend Message Sound
    if (this.addon.configuration().getAutomationConfig().friendMessageSound().get()) {
      if (msg.matches("\\[Friend\\] ([a-zA-Z0-9_]{2,16}) -> Me: .*")) {
        this.addon.labyAPI().minecraft().sounds().playSound(
            ResourceLocation.create("minecraft", "entity.experience_orb.pickup"), 1000, 1);
        return;
      }
    }

    // Start of game
    if (msg.equals("Let the games begin!")) {
      this.manager.setInPreLobby(false);
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
        this.addon.labyAPI().minecraft().chatExecutor().chat(config.getGameEndMessage().get().msg, false);
        if (!config.getCustomMessage().isDefaultValue()) {
          this.addon.labyAPI().minecraft().chatExecutor().chat(config.getCustomMessage().get(), false);
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

    Matcher whereAmIMatcher = this.WhereAmIOutPut.matcher(msg);
    if (whereAmIMatcher.matches()) {
      this.manager.setBungeecord(whereAmIMatcher.group(1));
      this.manager.setServerID(whereAmIMatcher.group(2));
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
}
