package org.cubepanion.core.listener.chat;

import static org.cubepanion.core.utils.Utils.chestLocationsComponent;

import art.ameliah.libs.weave.ChestAPI.ChestLocation;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.concurrent.task.Task;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.Cubepanionconfig;
import org.cubepanion.core.config.subconfig.EndGameSubConfig;
import org.cubepanion.core.config.subconfig.EndGameSubConfig.GameEndMessage;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.managers.submanagers.FriendTrackerManager;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.utils.OnlineFriendLocation;
import org.cubepanion.core.versionlinkers.ChestFinderLink;
import org.cubepanion.core.versionlinkers.VotingLink;

public class Automations {

  private final Cubepanion addon;
  private final CubepanionManager manager;
  private final VotingLink votingLink;
  private final Task autoVoteTask;
  private final ChestFinderLink chestFinderLink;
  private final Task chestFinderTast;

  private final Task startOfGameTask;

  private final Component voteReminderComponent = Component.translatable(
      "cubepanion.messages.voteReminder", Colours.Primary);

  private final Pattern playerElimination = Pattern.compile(
      "([a-zA-Z0-9_]{2,16}) has been eliminated from the game\\.");
  private final Pattern EggWarsTeamJoin = Pattern.compile("You have joined .{1,30} team\\.");
  private final Pattern WhereAmIOutPut = Pattern.compile(
      "You are on proxy: (\\w{0,2}bungeecord\\d{1,3})\\nYou are on server: (.{5})");
  private final Pattern FriendListTop = Pattern.compile(
      "------- Friends \\(\\d{1,10}\\/\\d{1,10}\\) -------");
  private final Pattern FriendListOffline = Pattern.compile(
      "^(?:[a-zA-Z0-9_]{2,16}, )*[a-zA-Z0-9_]{2,16}$");
  private final Pattern onlineFriends = Pattern.compile(
      "(?<username>[a-zA-Z0-9_]{2,16}) - (?:Playing|Online on)(?: Team| Main|)? (?<game>[a-zA-Z ]*?)(?: in| #\\d{1,2}|)? ?(?:map|\\[[A-Z]{2}\\])? ?(?<map>[a-zA-Z]*)?");
  private final Pattern fiveSecondsRemaining = Pattern.compile(
      "[a-zA-Z ]{0,30} is starting in 5 seconds\\.");
  private boolean voted = false;
  private boolean friendListBeingSend = false;

  public Automations(Cubepanion addon, VotingLink votingLink, ChestFinderLink chestFinderLink) {
    this.addon = addon;
    this.manager = addon.getManager();
    this.votingLink = votingLink;
    this.chestFinderLink = chestFinderLink;

    this.autoVoteTask = Task.builder(() -> {
      if (this.votingLink != null) {
        this.votingLink.vote(this.manager.getDivision(),
            this.addon.configuration().getAutoVoteSubConfig());
      }
    }).delay(100, TimeUnit.MILLISECONDS).build();

    this.chestFinderTast = Task.builder(() -> {
      if (this.manager.getDivision().equals(CubeGame.LOBBY)) {
        List<ChestLocation> chestLocations = this.chestFinderLink.getChestLocations();
        if (!chestLocations.isEmpty()) {
          for (ChestLocation loc : chestLocations) {
            addon.displayMessage(chestLocationsComponent(loc));
          }
        }
      }
    }).delay(1000, TimeUnit.MILLISECONDS).build();

    this.startOfGameTask = Task.builder(() -> {
      if (this.addon.configuration().getEggWarsMapInfoSubConfig().isEnabled().get()
          && this.addon.getManager().getDivision().equals(CubeGame.TEAM_EGGWARS)) {
        this.manager.getEggWarsMapInfoManager().doEggWarsMapLayout();
      }
      this.addon.rpcManager.startOfGame();
      this.addon.rpcManager.updateRPC();
    }).delay(1000, TimeUnit.MILLISECONDS).build();
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }
    Minecraft minecraft = this.addon.labyAPI().minecraft();
    String msg = e.chatMessage().getPlainText();
    ClientPlayer p = minecraft.getClientPlayer();
    if (p == null) {
      return;
    }

    Cubepanionconfig mainConfig = this.addon.configuration();

    String playerRegex = ".{0,5}" + p.getName() + ".{0,5}";

    // Friend Message Sound
    if (mainConfig.getAutomationConfig().friendMessageSound().get()) {
      if (msg.matches("\\[Friend\\] ([a-zA-Z0-9_]{2,16}) -> Me : .*")) {
        minecraft.sounds()
            .playSound(mainConfig.getAutomationConfig().getFriendMessageSoundId(), 100, 1);
        return;
      }
    }

    // 5 seconds remaining
    if (this.fiveSecondsRemaining.matcher(msg).matches()) {
      if (!this.voted && mainConfig.getQolConfig().getReminderToVote().get()) {
        minecraft.sounds()
            .playSound(mainConfig.getQolConfig().getVoteReminderResourceLocation(), 100, 1);
        minecraft.chatExecutor().displayClientMessage(this.voteReminderComponent);
      }
      this.voted = false;
      return;
    }

    // Start of game
    if (msg.equals("Let the games begin!")) {
      this.manager.setInPreLobby(false);
      this.manager.setGameStartTime(System.currentTimeMillis());
      this.startOfGameTask.execute();
      return;
    }

    // RPC
    Matcher matcher = playerElimination.matcher(msg);
    if (matcher.matches()) {
      this.addon.rpcManager.registerDeath(matcher.group(1));
    }

    // Auto GG
    EndGameSubConfig config = this.addon.configuration().getAutomationConfig()
        .getEndGameSubConfig();
    if (config.isEnabled().get() && !manager.isEliminated()) {
      String eliminationMessage = playerRegex + " has been eliminated from the game.";
      if (msg.equals("Congratulations, you win!") || (msg.matches(eliminationMessage)
          && config.getOnElimination().get())) {
        GameEndMessage gameEndMessage = config.getGameEndMessage().get();
        if (gameEndMessage != GameEndMessage.NONE) {
          minecraft.chatExecutor().chat(this.gameEndMessagesToReadable(gameEndMessage), false);
        }
        if (!config.getCustomMessage().isDefaultValue()) {
          minecraft.chatExecutor().chat(
              (this.manager.getPartyManager().isPartyChat() ? "!" : "") + config.getCustomMessage()
                  .get(), false);
        }
        manager.setEliminated(true);
        return;
      }
    }

    // Spawn protection countdown
    if (msg.equals("You are now invincible for 10 seconds.")) {
      this.addon.getManager().getSpawnProtectionManager().getClientPlayerSpawnProtection()
          .registerDeath();
      return;
    }

    // TeamColour Tracker
    Matcher teamColourMatcher = this.EggWarsTeamJoin.matcher(msg);
    if (teamColourMatcher.matches()) {
      List<Component> children = e.chatMessage().component().getChildren();
      if (children.size() == 0) {
        this.manager.setTeamColour("yellow");
      } else {
        this.manager.setTeamColour(children.get(0).getColor().toString());
      }
    }

    // Bungee & serverid matcher
    Matcher whereAmIMatcher = this.WhereAmIOutPut.matcher(msg);
    if (whereAmIMatcher.matches()) {
      this.manager.setBungeecord(whereAmIMatcher.group(1));
      this.manager.setServerID(whereAmIMatcher.group(2));
      return;
    }

    // Vote warn
    String voteMessage = playerRegex + " voted for .*\\. \\d{1,4} votes?";
    if (msg.matches(voteMessage) && this.manager.isInPreLobby()) {
      this.voted = true;
      return;
    }

    // Friends list shorter && tracker
    FriendTrackerManager friendTrackerManager = this.manager.getFriendTrackerManager();
    if (friendTrackerManager.isUpdating()) {
      if (this.FriendListTop.matcher(msg).matches()) {
        e.setCancelled(true);
        return;
      }

      Matcher onlineFriends = this.onlineFriends.matcher(msg);
      if (onlineFriends.matches()) {
        friendTrackerManager.updateFriendLocation(
            new OnlineFriendLocation(
                onlineFriends.group("username"),
                onlineFriends.group("game"),
                onlineFriends.group("map")));
        e.setCancelled(true);
        return;
      }

      if (msg.equals("Offline: ")) {
        e.setCancelled(true);
        return;
      }

      if (this.FriendListOffline.matcher(msg).matches()) {
        e.setCancelled(true);
        friendTrackerManager.setUpdating(false);
        return;
      }
    }

    if (mainConfig.getQolConfig().getShortFriendsList().get()) {

      if (this.FriendListTop.matcher(msg).matches()) {
        this.friendListBeingSend = true;
        return;
      }

      if (this.friendListBeingSend) {
        if (msg.equals("Offline: ") && !this.manager.hasRequestedFullFriendsList()) {
          e.setCancelled(true);
          return;
        }

        if (this.FriendListOffline.matcher(msg).matches()) {
          this.friendListBeingSend = false;
          if (this.manager.hasRequestedFullFriendsList()) {
            this.manager.setRequestedFullFriendsList(false);
          } else {
            e.setCancelled(true);
            return;
          }
        }
      }
    }

    if (mainConfig.getAutoVoteSubConfig().isEnabled()) {
      String joinRegex = "\\[\\+\\]" + playerRegex + " joined your game \\(\\d{1,3}/\\d{1,3}\\)\\.";
      if (msg.matches(joinRegex)) {
        Task toRun = this.autoVoteTask;
        Timer timer = new Timer("waitingForNoneLobbyDivision");
        timer.schedule(new TimerTask() {
          private int count = 0;

          @Override
          public void run() {
            count++;
            if (count == 10) {
              timer.cancel();
            }
            if (!Cubepanion.get().getManager().getDivision().equals(CubeGame.LOBBY)) {
              timer.cancel();
              toRun.execute();
            }
          }
        }, 100, 100);
      }
    }

    if (mainConfig.getQolConfig().getChestLocation().get() && this.chestFinderLink != null) {
      if (msg.equals("A chest has been hidden somewhere in the Lobby with some goodies inside!")) {
        this.chestFinderTast.execute();
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
