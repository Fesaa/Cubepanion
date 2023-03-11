package org.cubecraftutilities.core.utils.imp;

import java.util.Arrays;
import java.util.List;
import net.labymod.api.client.component.Component;
import org.cubecraftutilities.core.Colours;
import org.cubecraftutilities.core.utils.ColourConverters;
import org.cubecraftutilities.core.utils.imp.base.EggWarsMap;
import org.cubecraftutilities.core.utils.imp.base.GenLayout;

public class DoubleCrossEggWarsMap implements EggWarsMap {

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
  public DoubleCrossEggWarsMap(String mapName, int teamSize, int buildLimit, GenLayout genLayout,  List<String>... teamColours) {
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
    return Component.text("Build limit: ", Colours.Primary)
        .append(Component.text(this.buildLimit, Colours.Secondary));
  }

  @Override
  public String getPartyMessage() {
    return "@Side: "
        + ColourConverters.colourToCubeColour(this.teamSide)
        + ColourConverters.colourToCubeColourString(this.teamSide)
        + "&r. Left: "
        + ColourConverters.colourToCubeColour(this.teamLeftLeft)
        + ColourConverters.colourToCubeColourString(this.teamLeftLeft)
        + "&r &"
        + ColourConverters.colourToCubeColour(this.teamLeftRight)
        + ColourConverters.colourToCubeColourString(this.teamLeftRight)
        + "&r. Right: "
        + ColourConverters.colourToCubeColour(this.teamRightLeft)
        + ColourConverters.colourToCubeColourString(this.teamRightLeft)
        + "&r &"
        + ColourConverters.colourToCubeColour(this.teamRightRight)
        + ColourConverters.colourToCubeColourString(this.teamRightRight)
        + "&r. Across: "
        + ColourConverters.colourToCubeColour(this.teamAcrossLeft)
        + ColourConverters.colourToCubeColourString(this.teamAcrossLeft)
        + "&r &"
        + ColourConverters.colourToCubeColour(this.teamAcrossRight)
        + ColourConverters.colourToCubeColourString(this.teamAcrossRight)
        + "&r.";
  }

  @Override
  public void setCurrentTeamColour(String teamColour) {
    IndexPair indexPair = this.getIndex(teamColour);

    if (indexPair.getGroup() == -1 || indexPair.getLeftRight() == -1) {
      return;
    }

    this.currentTeamColour = teamColour;
    this.teamSide = this.teamColours.get(indexPair.getGroup()).get((indexPair.getLeftRight() + 1) % 2);
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

  @SuppressWarnings("DuplicatedCode")
  public IndexPair getIndex(String member) {

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
