package org.cubepanion.core.utils.eggwarsmaps;

import java.util.Arrays;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.I18n;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;
import org.cubepanion.core.utils.eggwarsmaps.base.EggWarsMap;
import org.cubepanion.core.utils.eggwarsmaps.base.GenLayout;

public class DoubleCrossEggWarsMap implements EggWarsMap {

  public final String mainKey =
      I18nNamespaces.managerNameSpace + "EggWarsMapInfoManager.directions";
  private final Component teamFillerSpaces = Component.text("      ");
  private final Component sideSpaces = Component.text("  ");
  private final Component betweenSpaces = Component.text("    ");

  private final String mapName;
  private final int teamSize;
  private final int buildLimit;
  private final GenLayout genLayout;

  private final List<List<String>> teamColours;

  private String currentTeamColour = "";
  private String teamSide = "";
  private String teamLeftLeft = "";
  private String teamLeftRight = "";
  private String teamRightLeft = "";
  private String teamRightRight = "";
  private String teamAcrossLeft = "";
  private String teamAcrossRight = "";

  @SafeVarargs
  public DoubleCrossEggWarsMap(String mapName, int teamSize, int buildLimit, GenLayout genLayout,
      List<String>... teamColours) {
    this.mapName = mapName;
    this.teamSize = teamSize;
    this.buildLimit = buildLimit;
    this.genLayout = genLayout;
    this.teamColours = Arrays.asList(teamColours);
    this.setCurrentTeamColour(this.teamColours.get(0).get(0));
  }

  @Override
  public String getName() {
    return this.mapName;
  }

  @Override
  public Component getGenLayoutComponent() {
    return this.genLayout.getLayoutComponent();
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
  public Component getBuildLimitMessage() {
    return Component.translatable(
            I18nNamespaces.managerNameSpace + "EggWarsMapInfoManager.buildLimit", Colours.Primary)
        .append(Component.text(this.buildLimit, Colours.Secondary));
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
    IndexPair indexPair = this.getIndex(teamColour);

    if (indexPair.getGroup() == -1 || indexPair.getLeftRight() == -1) {
      return;
    }

    this.currentTeamColour = teamColour;
    this.teamSide = this.teamColours.get(indexPair.getGroup())
        .get((indexPair.getLeftRight() + 1) % 2);
    this.teamLeftLeft = this.teamColours.get((indexPair.getGroup() + 3) % 4).get(0);
    this.teamLeftRight = this.teamColours.get((indexPair.getGroup() + 3) % 4).get(1);
    this.teamRightLeft = this.teamColours.get((indexPair.getGroup() + 1) % 4).get(0);
    this.teamRightRight = this.teamColours.get((indexPair.getGroup() + 1) % 4).get(1);
    this.teamAcrossLeft = this.teamColours.get((indexPair.getGroup() + 2) % 4).get(0);
    this.teamAcrossRight = this.teamColours.get((indexPair.getGroup() + 2) % 4).get(1);

    if (indexPair.getLeftRight() == 1) {
      this.currentTeamColour = this.teamSide;
      this.teamSide = teamColour;
    }
  }

  IndexPair getIndex(String member) {

    int leftRight = 0;
    int groupIndex = 0;

    for (List<String> group : this.teamColours) {
      for (String colour : group) {
        if (colour.equals(member)) {
          return new IndexPair(leftRight, groupIndex);
        }
        leftRight++;
      }
      groupIndex++;
      leftRight = 0;
    }

    return new IndexPair(-1, -1);
  }

  static final class IndexPair {

    private final int leftRight;
    private final int group;

    public IndexPair(int first, int second) {
      this.leftRight = first;
      this.group = second;
    }

    public int getLeftRight() {
      return leftRight;
    }

    public int getGroup() {
      return group;
    }
  }

}
