package org.cubepanion.core.utils.eggwarsmaps;

import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.I18n;
import net.labymod.api.util.Pair;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;
import org.cubepanion.core.utils.eggwarsmaps.base.EggWarsMap;
import org.cubepanion.core.utils.eggwarsmaps.base.GenLayout;

import static org.cubepanion.core.utils.Utils.getDoubleIndex;

public class DoubleCrossEggWarsMap extends EggWarsMap {

  private final List<List<String>> teamColours;
  private String teamSide = "";
  private String teamLeftLeft = "";
  private String teamLeftRight = "";
  private String teamRightLeft = "";
  private String teamRightRight = "";
  private String teamAcrossLeft = "";
  private String teamAcrossRight = "";

  public DoubleCrossEggWarsMap(String mapName, int teamSize, int buildLimit, GenLayout genLayout,
      List<List<String>> teamColours) {
    super(mapName, teamSize, buildLimit, genLayout);
    this.teamColours = teamColours;
    this.setCurrentTeamColour(this.teamColours.get(0).get(0));
  }

  @Override
  public Component getMapLayoutComponent() {
    return Component.empty()
        .append(this.sideSpaces)
        .append(this.teamFillerSpaces)
        .append(this.betweenSpaces)
        .append(this.getTeamFiller(this.teamAcrossLeft))
        .append(this.betweenSpaces)
        .append(this.getTeamFiller(this.teamAcrossRight))
        .append(Component.newline())
        .append(this.sideSpaces)
        .append(this.getTeamFiller(this.teamLeftLeft))
        .append(this.betweenSpaces)
        .append(this.teamFillerSpaces)
        .append(this.betweenSpaces)
        .append(this.teamFillerSpaces)
        .append(this.betweenSpaces)
        .append(this.getTeamFiller(this.teamRightRight))
        .append(Component.newline())
        .append(this.sideSpaces)
        .append(this.getTeamFiller(this.teamLeftRight))
        .append(this.betweenSpaces)
        .append(this.teamFillerSpaces)
        .append(this.betweenSpaces)
        .append(this.teamFillerSpaces)
        .append(this.betweenSpaces)
        .append(this.getTeamFiller(this.teamRightLeft))
        .append(Component.newline())
        .append(this.sideSpaces)
        .append(this.teamFillerSpaces)
        .append(this.betweenSpaces)
        .append(this.getTeamFiller(this.currentTeamColour))
        .append(this.betweenSpaces)
        .append(this.getTeamFiller(this.teamSide));
  }

  @Override
  public String getPartyMessage() {
    return "@"
        + I18n.translate(this.mainKey + "side")
        + Colours.colourToCubeColour(this.teamSide)
        + Colours.colourToCubeColourString(this.teamSide)
        + "&r. "
        + I18n.translate(this.mainKey + "left")
        + Colours.colourToCubeColour(this.teamLeftLeft)
        + Colours.colourToCubeColourString(this.teamLeftLeft)
        + "&r &"
        + Colours.colourToCubeColour(this.teamLeftRight)
        + Colours.colourToCubeColourString(this.teamLeftRight)
        + "&r. "
        + I18n.translate(this.mainKey + "right")
        + Colours.colourToCubeColour(this.teamRightLeft)
        + Colours.colourToCubeColourString(this.teamRightLeft)
        + "&r &"
        + Colours.colourToCubeColour(this.teamRightRight)
        + Colours.colourToCubeColourString(this.teamRightRight)
        + "&r. "
        + I18n.translate(this.mainKey + "across")
        + Colours.colourToCubeColour(this.teamAcrossLeft)
        + Colours.colourToCubeColourString(this.teamAcrossLeft)
        + "&r &"
        + Colours.colourToCubeColour(this.teamAcrossRight)
        + Colours.colourToCubeColourString(this.teamAcrossRight)
        + "&r.";
  }

  @Override
  public void setCurrentTeamColour(String teamColour) {
    var indexPair = getDoubleIndex(this.teamColours, teamColour);
    Integer first = indexPair.getFirst();
    Integer second = indexPair.getSecond();
    if (first == null || second == null) {
      return;
    }
    if (first == -1 || second == -1) {
      return;
    }

    this.currentTeamColour = teamColour;
    this.teamSide = this.teamColours.get(first).get((second + 1) % 2);
    this.teamLeftLeft = this.teamColours.get((first + 3) % 4).get(0);
    this.teamLeftRight = this.teamColours.get((first + 3) % 4).get(1);
    this.teamRightLeft = this.teamColours.get((first + 1) % 4).get(0);
    this.teamRightRight = this.teamColours.get((first + 1) % 4).get(1);
    this.teamAcrossLeft = this.teamColours.get((first + 2) % 4).get(0);
    this.teamAcrossRight = this.teamColours.get((first + 2) % 4).get(1);

    if (second == 1) {
      this.currentTeamColour = this.teamSide;
      this.teamSide = teamColour;
    }
  }
}
