package org.cubepanion.core.listener.chat;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.CubepanionConfig;
import org.cubepanion.core.config.subconfig.EndGameSubConfig;
import org.cubepanion.core.config.subconfig.EndGameSubConfig.GameEndMessage;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.managers.DiscordAPI;

public class Automations {

  private final Cubepanion addon;
  private final CubepanionManager manager;
  private final Pattern playerElimination = Pattern.compile(
      "([a-zA-Z0-9_]{2,16}) has been eliminated from the game\\.");
  private final Pattern EggWarsTeamJoin = Pattern.compile("You have joined .{1,30} team\\.");
  private final Pattern WhereAmIOutPut = Pattern.compile(
      "You are on proxy: (\\w{0,2}bungeecord\\d{1,3})\\nYou are on server: (.{5})");
  private final Pattern FriendListTop = Pattern.compile(
      "------- Friends \\(\\d{1,10}\\/\\d{1,10}\\) -------");
  private final Pattern FriendListOffline = Pattern.compile(
      "^(?:[a-zA-Z0-9_]{2,16}, )*[a-zA-Z0-9_]{2,16}$");
  private final Pattern whoList = Pattern.compile(
      "[:|,] (?<rankstring>.) (?:.{0,5} |)(?<username>[a-zA-Z0-9_]{2,16})(?: .{0,5}|)");
  private boolean friendListBeingSend = false;

  public Automations(Cubepanion addon) {
    this.addon = addon;
    this.manager = addon.getManager();
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

    CubepanionConfig mainConfig = this.addon.configuration();

    String playerRegex = ".{0,5}" + p.getName() + ".{0,5}";

    // Start of game
    if (msg.equals("Let the games begin!")) {
      this.manager.setInPreLobby(false);
      this.manager.setGameStartTime(System.currentTimeMillis());
      return;
    }

    // RPC
    Matcher matcher = playerElimination.matcher(msg);
    if (matcher.matches()) {
      DiscordAPI.getInstance().registerDeath(matcher.group(1));
    }

    // Spawn protection countdown
    if (msg.equals("You are now invincible for 10 seconds.")) {
      manager.getSpawnProtectionManager().getClientPlayerSpawnProtection().registerDeath();
      return;
    }

    // TeamColour Tracker
    Matcher teamColourMatcher = this.EggWarsTeamJoin.matcher(msg);
    if (teamColourMatcher.matches()) {
      List<Component> children = e.chatMessage().component().getChildren();
      if (children.isEmpty()) {
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

    // Friend list shortener
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

    if (manager.hasRequestedRankString()) {
      Matcher whoListMatcher = this.whoList.matcher(msg);
      while (whoListMatcher.find()) {
        String username = whoListMatcher.group("username");
        String rankString = whoListMatcher.group("rankstring");
        if (username.equals(p.getName())) {
          manager.setRankString(rankString);
          manager.setRequestedRankString(false);
          e.setCancelled(true);
          break;
        }
      }
    }

  }
}
