package org.ccu.core.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.labymod.api.client.scoreboard.DisplaySlot;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.scoreboard.ScoreboardObjective;
import org.ccu.core.CCU;
import org.ccu.core.config.internal.CCUinternalConfig;
import java.util.ArrayList;

public class EggWarsMapInfo {

  private static final String teamFiller = "||||||";

  private static String colourToUnicode(String colour) {
    switch (colour) {
      case "black": {return "§0";}
      case "dark_blue": {return "§1";}
      case "dark_green": {return "§2";}
      case "dark_aqua": {return "§3";}
      case "dark_red": {return "§4";}
      case "dark_purple": {return "§5";}
      case "gold": {return "§6";}
      case "gray": {return "§7";}
      case "dark_gray": {return "§8";}
      case "blue": {return "§9";}
      case "green": {return "§a";}
      case "aqua": {return "§b";}
      case "red": {return "§c";}
      case "light_purple": {return "§d";}
      case "yellow": {return "§e";}
      case "white": {return "§f";}
    }
    return "";
  }

  private static String colourToCubeColour(String colour) {
    switch (colour) {
      case "black": {return "&0";}
      case "dark_blue": {return "&1";}
      case "dark_green": {return "&2";}
      case "dark_aqua": {return "&3";}
      case "dark_red": {return "&4";}
      case "dark_purple": {return "&5";}
      case "gold": {return "&6";}
      case "gray": {return "&7";}
      case "dark_gray": {return "&8";}
      case "blue": {return "&9";}
      case "green": {return "&a";}
      case "aqua": {return "&b";}
      case "red": {return "&c";}
      case "light_purple": {return "&d";}
      case "yellow": {return "&e";}
      case "white": {return "&f";}
    }
    return "";
  }

  private static String colourToCubeColourString(String colour) {
    switch (colour) {
      case "black":
      case "gray":
      case "dark_gray": {return "Black";}
      case "dark_blue":
      case "blue": {return "Blue";}
      case "dark_green":
      case "green": {return "Green";}
      case "dark_red":
      case "red": {return "Red";}
      case "dark_aqua": {return "Aqua";}
      case "dark_purple": {return "Purple";}
      case "gold": {return "Orange";}
      case "aqua": {return "Light Blue";}
      case "light_purple": {return "Pink";}
      case "yellow": {return "Yellow";}
      case "white": {return "White";}
    }
    return "";
  }

  public static void eggWarsMapInfo(CCU addon) {
    Scoreboard scoreboard = addon.labyAPI().minecraft().getScoreboard();
    if (scoreboard == null) {return;};
    ScoreboardObjective scoreboardObjective = scoreboard.objective(DisplaySlot.SIDEBAR);
    if (scoreboardObjective == null) {return;};
    CCUinternalConfig.updateTeamColour(addon);

    addon.logger().info(CCUinternalConfig.debugString());

    ArrayList<String> mapInfo = EggWarsMapInfo.MakeMapLayout(addon, CCUinternalConfig.map, CCUinternalConfig.teamColour);

    if (mapInfo == null) {return;}

    if (addon.configuration().getEggWarsMapInfoSubConfig().getMapLayout().get()) {
      addon.labyAPI().minecraft().chatExecutor().displayClientMessage(mapInfo.get(0));
    }

    if (addon.configuration().getEggWarsMapInfoSubConfig().getBuildLimit().get() && addon.CCUconfig.mapInfo.get("teamBuildLimit").getAsJsonObject().has(CCUinternalConfig.map)) {
      String buildLimit = "§6The build limit is: " + addon.CCUconfig.mapInfo.get("teamBuildLimit").getAsJsonObject().get(CCUinternalConfig.map).getAsString();
      addon.labyAPI().minecraft().chatExecutor().displayClientMessage(buildLimit);
    }

    if (addon.configuration().getEggWarsMapInfoSubConfig().getLogInParty().get() && CCUinternalConfig.partyStatus && mapInfo.size() > 1) {
      addon.labyAPI().minecraft().chatExecutor().chat(mapInfo.get(1), false);
    }
  }

  private static ArrayList<String> MakeMapLayout(CCU addon, String mapName, String teamColour) {
    if (!addon.CCUconfig.mapInfo.get("teamColourOrder").getAsJsonObject().has(mapName)) {
      return null;
    }
    JsonObject MapInfo = addon.CCUconfig.mapInfo.get("teamColourOrder").getAsJsonObject().get(mapName).getAsJsonObject();

    switch (MapInfo.get("style").getAsString()) {
      case "cross": return MakeMapLayoutCross(teamColour, MapInfo);
      case "square": return MakeMapLayoutSquare(teamColour, MapInfo);
      case "double_triangle": return MakeMapLayoutDoubleTriangle(teamColour, MapInfo);
      case "double_cross": return MakeMapLayoutDoubleCross(teamColour, MapInfo);
    }
    return null;
  }

  private static ArrayList<String> MakeMapLayoutDoubleCross(String teamColour, JsonObject mapInfo) {
    JsonArray mapLayout = mapInfo.get("layout").getAsJsonArray();

    int leftRight = -1;
    int groupIndex = -1;

    for (int groupCounter = 0; groupCounter < mapLayout.getAsJsonArray().size(); groupCounter++) {
      for (int leftRightCounter = 0; leftRightCounter < mapLayout.getAsJsonArray().get(groupCounter).getAsJsonArray().size(); leftRightCounter++) {
        if (mapLayout.getAsJsonArray().get(groupCounter).getAsJsonArray().get(leftRightCounter).getAsString().equals(teamColour)) {
          leftRight = leftRightCounter;
          groupIndex = groupCounter;
        }
      }
    }

    String teamSide = mapLayout.getAsJsonArray().get(groupIndex).getAsJsonArray().get((leftRight + 1) % 2).getAsString();
    String teamLeftLeft = mapLayout.getAsJsonArray().get((groupIndex - 1) % 4).getAsJsonArray().get(0).getAsString();
    String teamLeftRight = mapLayout.getAsJsonArray().get((groupIndex - 1) % 4).getAsJsonArray().get(1).getAsString();
    String teamRightLeft = mapLayout.getAsJsonArray().get((groupIndex + 1) % 4).getAsJsonArray().get(0).getAsString();
    String teamRightRight = mapLayout.getAsJsonArray().get((groupIndex + 1) % 4).getAsJsonArray().get(1).getAsString();
    String teamAcrossLeft = mapLayout.getAsJsonArray().get((groupIndex + 2) % 4).getAsJsonArray().get(0).getAsString();
    String teamAcrossRight = mapLayout.getAsJsonArray().get((groupIndex + 2) % 4).getAsJsonArray().get(1).getAsString();

    if (leftRight == 1) {
      String temp = teamColour;
      teamColour = teamSide;
      teamSide = temp;
    }

    String mapLayoutString = "§dMap layout:\n\n" +
        spaceMaker(4 + teamFiller.length()) + colourToUnicode(teamAcrossRight) + teamFiller +
        spaceMaker(2) + colourToUnicode(teamAcrossLeft) + teamFiller + "\n" +
        spaceMaker(2) + colourToUnicode(teamLeftLeft) + teamFiller +
        spaceMaker(6 + 2 * teamFiller.length()) + colourToUnicode(teamRightRight) + teamFiller + "\n\n" +
        spaceMaker(2) + colourToUnicode(teamLeftRight) + teamFiller +
        spaceMaker(6 + 2 * teamFiller.length()) + colourToUnicode(teamRightLeft) + teamFiller + "\n" +
        spaceMaker(4 + teamFiller.length()) + colourToUnicode(teamColour) + teamFiller +
        spaceMaker(2) + colourToUnicode(teamSide) + teamFiller;

    ArrayList<String> out = new ArrayList<>();
    out.add(mapLayoutString);
    out.add(null);

    return out;
  }

  private static ArrayList<String> MakeMapLayoutDoubleTriangle(String teamColour, JsonObject mapInfo) {
    JsonArray mapLayout = mapInfo.get("layout").getAsJsonArray();


    String mapLayoutString;

    int teamTriangleLocation = -1;
    int teamLeftRight = -1;

    for (int triangleLocation = 0; triangleLocation < mapLayout.size(); triangleLocation++) {
      for (int leftRight = 0; leftRight < mapLayout.get(triangleLocation).getAsJsonArray().size(); leftRight++) {
        if (mapLayout.get(triangleLocation).getAsJsonArray().get(leftRight).getAsString().equals(teamColour)) {
          teamTriangleLocation = triangleLocation;
          teamLeftRight = leftRight;
          break;
        }
      }
    }

    String teamUnderLeft = "";
    String teamUnderRight = "";
    String teamLeftPoint = "";
    String teamRightPoint = "";
    String teamUpLeft = "";
    String teamUpRight = "";

    switch (teamTriangleLocation) {
      case 0:
        teamUnderLeft = mapLayout.get(0).getAsJsonArray().get(0).getAsString();
        teamUnderRight = mapLayout.get(0).getAsJsonArray().get(1).getAsString();
        teamLeftPoint = mapLayout.get(1).getAsJsonArray().get(0).getAsString();
        teamRightPoint = mapLayout.get(1).getAsJsonArray().get(1).getAsString();
        teamUpLeft = mapLayout.get(2).getAsJsonArray().get(1).getAsString();
        teamUpRight = mapLayout.get(2).getAsJsonArray().get(0).getAsString();
        break;
      case 1:
        if (teamLeftRight == 0) {
          teamUnderLeft = mapLayout.get(0).getAsJsonArray().get(0).getAsString();
          teamUnderRight = mapLayout.get(0).getAsJsonArray().get(1).getAsString();
          teamLeftPoint = mapLayout.get(1).getAsJsonArray().get(0).getAsString();
          teamRightPoint = mapLayout.get(1).getAsJsonArray().get(1).getAsString();
          teamUpLeft = mapLayout.get(2).getAsJsonArray().get(1).getAsString();
          teamUpRight = mapLayout.get(2).getAsJsonArray().get(0).getAsString();
        } else {
          teamUnderLeft = mapLayout.get(2).getAsJsonArray().get(0).getAsString();
          teamUnderRight = mapLayout.get(2).getAsJsonArray().get(1).getAsString();
          teamLeftPoint = mapLayout.get(1).getAsJsonArray().get(1).getAsString();
          teamRightPoint = mapLayout.get(1).getAsJsonArray().get(0).getAsString();
          teamUpLeft = mapLayout.get(0).getAsJsonArray().get(1).getAsString();
          teamUpRight = mapLayout.get(0).getAsJsonArray().get(0).getAsString();
        }
      break;
      case 2:
        teamUnderLeft = mapLayout.get(2).getAsJsonArray().get(0).getAsString();
        teamUnderRight = mapLayout.get(2).getAsJsonArray().get(1).getAsString();
        teamLeftPoint = mapLayout.get(1).getAsJsonArray().get(1).getAsString();
        teamRightPoint = mapLayout.get(1).getAsJsonArray().get(0).getAsString();
        teamUpLeft = mapLayout.get(0).getAsJsonArray().get(1).getAsString();
        teamUpRight = mapLayout.get(0).getAsJsonArray().get(0).getAsString();
      break;
    }

    if (teamTriangleLocation != 1) {
      mapLayoutString = "§dMap layout:\n\n" +
          spaceMaker(4 + teamFiller.length()) + colourToUnicode(teamUpLeft) + teamFiller +
          spaceMaker(6) + colourToUnicode(teamUpRight) + teamFiller + "\n" +
          spaceMaker(2) + colourToUnicode(teamLeftPoint) + teamFiller +
          spaceMaker(10 + 2 * teamFiller.length()) + colourToUnicode(teamRightPoint) + teamFiller + "\n" +
          spaceMaker(4 + teamFiller.length()) + colourToUnicode(teamUnderLeft) + teamFiller +
          spaceMaker(6) + colourToUnicode(teamUnderRight) + teamFiller + "\n";
    } else {
      mapLayoutString = "§dMap layout:\n\n" +
          spaceMaker(2 + teamFiller.length()) + colourToUnicode(teamRightPoint) + teamFiller + "\n" +
          spaceMaker(2) + colourToUnicode(teamUpRight) + teamFiller +
          spaceMaker(2 + teamFiller.length()) + colourToUnicode(teamUnderRight) + teamFiller + "\n\n" +
          spaceMaker(2) + colourToUnicode(teamUpLeft) + teamFiller +
          spaceMaker(2 + teamFiller.length()) + colourToUnicode(teamUnderLeft) + teamFiller + "\n" +
          spaceMaker(2 + teamFiller.length()) + colourToUnicode(teamLeftPoint) + teamFiller;
    }


    ArrayList<String> out = new ArrayList<>();
    out.add(mapLayoutString);
    out.add(null);

    return out;
  }

  private static ArrayList<String> MakeMapLayoutCross(String teamColour, JsonObject mapInfo) {

    JsonArray mapLayout = mapInfo.get("layout").getAsJsonArray();
    ArrayList<String> formattedMapLayout = new ArrayList<>();
    int teamIndex = -1;
    for (int i = 0; i < mapLayout.size(); i++) {
      formattedMapLayout.add(mapLayout.get(i).getAsString());
      if (mapLayout.get(i).getAsString().equals(teamColour)) {
        teamIndex = i;
      }
    }

    String teamLeft = formattedMapLayout.get((teamIndex + 1) % formattedMapLayout.size());
    String teamBefore = formattedMapLayout.get((teamIndex + 2) % formattedMapLayout.size());
    String teamRight = formattedMapLayout.get((teamIndex + 3) % formattedMapLayout.size());

    StringBuilder partyMapLayoutString = new StringBuilder();

    String mapLayoutString = "§dMap layout:\n\n" +
        spaceMaker(4 + teamFiller.length()) + colourToUnicode(teamBefore) + teamFiller + "\n" +
        spaceMaker(2) + colourToUnicode(teamLeft) + teamFiller + spaceMaker(teamFiller.length() + 7) +
        colourToUnicode(teamRight) + teamFiller + "\n" +
        spaceMaker(4 + teamFiller.length()) + colourToUnicode(teamColour) + teamFiller + "\n";

    partyMapLayoutString.append("@Left: ").append(colourToCubeColour(teamLeft)).append(colourToCubeColourString(teamLeft))
        .append("&r. Right: ").append(colourToCubeColour(teamRight)).append(colourToCubeColourString(teamRight))
        .append("&r. In Front: ").append(colourToCubeColour(teamBefore)).append(colourToCubeColourString(teamBefore))
        .append("&r.");

    ArrayList<String> out = new ArrayList<>();
    out.add(mapLayoutString);
    out.add(String.valueOf(partyMapLayoutString));

    return out;
  }

  private static ArrayList<String> MakeMapLayoutSquare(String teamColour, JsonObject mapInfo) {

    JsonArray mapLayout = mapInfo.get("layout").getAsJsonArray();
    int teamIndexSide = -1;
    int teamIndexDepth = -1;
    for (int i = 0; i < mapLayout.size(); i++) {
      for (int j = 0; j < mapLayout.get(i).getAsJsonArray().size(); j++) {
        if (mapLayout.get(i).getAsJsonArray().get(j).getAsString().equals(teamColour)) {
          teamIndexSide = i;
          teamIndexDepth = j;
        }

      }
    }

    String teamAcross = mapLayout.get((teamIndexSide + 1) % mapLayout.size()).getAsJsonArray().get(teamIndexDepth).getAsString();
    String teamSide = mapLayout.get(teamIndexSide).getAsJsonArray().get((teamIndexDepth + 1) % mapLayout.get(teamIndexSide).getAsJsonArray().size()).getAsString();
    String teamSideAcross =mapLayout.get((teamIndexSide + 1) % mapLayout.size()).getAsJsonArray().get((teamIndexDepth + 1) % mapLayout.get(teamIndexSide).getAsJsonArray().size()).getAsString();

    StringBuilder partyMapLayoutString = new StringBuilder();

    if (teamIndexDepth == 1) {
      if (teamIndexSide == 0) {
        String tempSide = teamColour;
        teamColour = teamSide;
        teamSide = tempSide;
      } else {
        String tempAcross = teamSideAcross;
        teamSideAcross = teamAcross;
        teamAcross = tempAcross;
      }
    } else {
      if (teamIndexSide == 0) {
        String tempAcross = teamSideAcross;
        teamSideAcross = teamAcross;
        teamAcross = tempAcross;
      } else {
        String tempSide = teamColour;
        teamColour = teamSide;
        teamSide = tempSide;
      }
    }

    String mapLayoutString = "§dMap layout:\n\n" +
        colourToUnicode(teamAcross) + spaceMaker(2) + teamFiller + spaceMaker(7) +
        colourToUnicode(teamSideAcross) + teamFiller + "\n\n" +
        colourToUnicode(teamColour) + spaceMaker(2) + teamFiller + spaceMaker(7) +
        colourToUnicode(teamSide) + teamFiller + "\n";

    partyMapLayoutString.append("@Across: ").append(colourToCubeColour(teamAcross)).append(colourToCubeColourString(teamAcross))
        .append("&r. Side: ").append(colourToCubeColour(teamSide)).append(colourToCubeColourString(teamSide))
        .append("&r Side & Across: ").append(colourToCubeColour(teamSideAcross)).append(colourToCubeColourString(teamSideAcross))
        .append("&r.");

    ArrayList<String> out = new ArrayList<>();
    out.add(mapLayoutString);
    out.add(String.valueOf(partyMapLayoutString));

    return out;
  }

  private static String spaceMaker(int n) {
    StringBuilder out = new StringBuilder();
    for (int i = 0; i < n; i++) {
      out.append(" ");
    }
    return out.toString();
  }

}
