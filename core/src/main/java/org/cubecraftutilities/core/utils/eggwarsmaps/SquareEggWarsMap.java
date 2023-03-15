package org.cubecraftutilities.core.utils.eggwarsmaps;

import java.util.Arrays;
import java.util.List;
import net.labymod.api.client.component.Component;
import org.cubecraftutilities.core.utils.Colours;
import org.cubecraftutilities.core.utils.eggwarsmaps.base.EggWarsMap;
import org.cubecraftutilities.core.utils.eggwarsmaps.base.GenLayout;

public class SquareEggWarsMap implements EggWarsMap {

  private final Component sideSpaces = Component.text("  ");
  private final Component betweenSpaces = Component.text("    ");

  private final String mapName;
  private final int teamSize;
  private final int buildLimit;
  private final GenLayout genLayout;

  private final List<List<String>> teamColours;

  private String currentTeamColour = "";
  private String teamSide = "";
  private String teamAcross = "";
  private String teamAcrossSide = "";

  @SafeVarargs
  public SquareEggWarsMap(String mapName, int teamSize, int buildLimit, GenLayout genLayout,  List<String>... teamColours) {
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
        .append(this.getTeamFiller(this.teamAcross))
        .append(this.betweenSpaces)
        .append(this.getTeamFiller(this.teamAcrossSide))
        .append(Component.newline())
        .append(Component.newline())
        .append(this.sideSpaces)
        .append(this.getTeamFiller(this.currentTeamColour))
        .append(this.betweenSpaces)
        .append(this.getTeamFiller(this.teamSide))
        ;
  }

  @Override
  public Component getBuildLimitMessage() {
    return Component.text("Build limit: ", Colours.Primary)
        .append(Component.text(this.buildLimit, Colours.Secondary));
  }

  @Override
  public String getPartyMessage() {
    return "@Side: "
        + Colours.colourToCubeColour(this.teamSide)
        + Colours.colourToCubeColourString(this.teamSide)
        + "&r. Across: "
        + Colours.colourToCubeColour(this.teamAcross)
        + Colours.colourToCubeColourString(this.teamAcross)
        + "&r. Across Side: "
        + Colours.colourToCubeColour(this.teamAcrossSide)
        + Colours.colourToCubeColourString(this.teamAcrossSide)
        + "&r.";
  }

  @Override
  public void setCurrentTeamColour(String teamColour) {
    IndexPair indexPair = this.getIndex(teamColour);

    if (indexPair.getSide() == -1 || indexPair.getLeftRight() == -1) {
      return;
    }

    this.currentTeamColour = teamColour;
    this.teamSide = this.teamColours.get(indexPair.getSide()).get((indexPair.getLeftRight() + 1) % 2);
    this.teamAcross = this.teamColours.get((indexPair.getSide() + 1) % 2).get((indexPair.getLeftRight() + 1) % 2);
    this.teamAcrossSide = this.teamColours.get((indexPair.getSide() + 1) % 2).get(indexPair.getLeftRight());

    if (indexPair.getLeftRight() == 1) {
      this.currentTeamColour = this.teamSide;
      this.teamSide = teamColour;

      String temp = this.teamAcross;
      this.teamAcross = this.teamAcrossSide;
      this.teamAcrossSide = temp;
    }
  }

  public IndexPair getIndex(String member) {

    int leftRight = 0;
    int side = 0;

    for (List<String> group : this.teamColours) {
      for (String colour : group) {
        if (colour.equals(member)) {
          return new IndexPair(leftRight, side);
        }
        leftRight++;
      }
      side++;
      leftRight = 0;
    }


    return new IndexPair(-1, -1);
  }

  static final class IndexPair {
    private final int leftRight;
    private final int side;

    public IndexPair(int leftRight, int side) {
      this.leftRight = leftRight;
      this.side = side;
    }

    public int getLeftRight() {
      return leftRight;
    }

    public int getSide() {
      return side;
    }
  }
}
