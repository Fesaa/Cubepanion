package org.ccu.core.config.internal;

import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.scoreboard.ScoreboardObjective;
import net.labymod.api.client.scoreboard.ScoreboardScore;
import org.ccu.core.CCU;
import org.jetbrains.annotations.NotNull;

public class CCUinternalConfig {

  public static String map = "";
  public static String teamColour = "";
  public static String name = "";
  public static String lastName = "";
  public static boolean won;
  public static String serverIP = "";
  public static boolean hasSaidGG = false;
  public static boolean inPreLobby = false;

  public static boolean partyStatus = false;

  public static int chestPartyAnnounce = 0;

  public static int totalHelmetDurability = 0;
  public static int totalChestPlateDurability = 0;
  public static int totalLeggingsDurability = 0;
  public static int totalBootsDurability = 0;

  public static String debugString() {
    return "\nDebug info for Needyfesa" +
        "\nMap: " + map +
        "\nteamColour: " + teamColour +
        "\nname: " + name +
        "\nserverIP: " + serverIP +
        "\npartyStatus: " + partyStatus +
        "\nhasSaidGG: " + hasSaidGG +
        "\nchestPartyAnnounce: " + chestPartyAnnounce +
        "\n";
  }

  public static void resetVars() {
    map = "";
    teamColour = "";
    name = "";
    serverIP = "";
    hasSaidGG = false;
    inPreLobby = false;

    partyStatus = false;

    chestPartyAnnounce = 0;
  }

  public static void setVars(CCU addon, @NotNull Scoreboard scoreboard, @NotNull ScoreboardObjective scoreboardObjective) {
    serverIP = "play.cubecraft.net";
    map = getMap(scoreboard, scoreboardObjective);
    lastName = name;
    name = ((TextComponent) scoreboardObjective.getTitle()).content();
    hasSaidGG = false;
    won = false;
  }

  public static void updateTeamColour(CCU addon) {
    NetworkPlayerInfo playerInfo = addon.labyAPI().minecraft().clientPlayer().networkPlayerInfo();
    if (playerInfo == null) {
      return;
    };
    for (Component component : playerInfo.getTeam().formatDisplayName(playerInfo.displayName()).children()) {
      if (!((TextComponent) component).content().equals("")) {
        teamColour = Objects.requireNonNull(((TextComponent) component).color()).toString();
        return;
      }
    }
  }

  private static String getMap(@NotNull Scoreboard scoreboard, ScoreboardObjective scoreboardObjective) {
    ScoreboardScore lastEntry = null;
    for (ScoreboardScore scoreboardScore : scoreboard.getScores(scoreboardObjective)) {
      if (scoreboardScore.getName().contains("Map:")) {
        if (lastEntry != null) {
          return lastEntry.getName().substring(2);
        }
      }
      lastEntry = scoreboardScore;
    }
    return "";
  }

}
