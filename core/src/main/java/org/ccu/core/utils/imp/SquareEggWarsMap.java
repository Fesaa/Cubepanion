package org.ccu.core.utils.imp;

import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.ccu.core.utils.ColourConverters;
import org.ccu.core.utils.imp.base.EggWarsMap;

public class SquareEggWarsMap implements EggWarsMap {

  private final Component teamFiller = Component.text("||||||");
  private final Component teamFillerSpaces = Component.text("      ");
  private final Component sideSpaces = Component.text("  ");
  private final Component betweenSpaces = Component.text("    ");

  private final String mapName;
  private final int teamSize;
  private final int buildLimit;

  private final List<List<String>> teamColours;

  private String currentTeamColour = "";
  private String teamSide = "";
  private String teamAcross = "";
  private String teamAcrossSide = "";

  @SafeVarargs
  public SquareEggWarsMap(String mapName, int teamSize, int buildLimit,  List<String>... teamColours) {
    this.mapName = mapName;
    this.teamSize = teamSize;
    this.buildLimit = buildLimit;
    this.teamColours = Arrays.asList(teamColours);
  }

  @Override
  public Component getMapLayoutComponent() {
    return Component.empty()
        .append(this.sideSpaces)
        .append(this.teamFiller.color(ColourConverters.colourToNamedTextColor(this.teamAcross)))
        .append(this.betweenSpaces)
        .append(this.teamFiller.color(ColourConverters.colourToNamedTextColor(this.teamAcrossSide)))
        .append(Component.newline())
        .append(Component.newline())
        .append(this.sideSpaces)
        .append(this.teamFiller.color(ColourConverters.colourToNamedTextColor(this.currentTeamColour)))
        .append(this.betweenSpaces)
        .append(this.teamFiller.color(ColourConverters.colourToNamedTextColor(this.teamSide)))
        ;
  }

  @Override
  public Component getBuildLimitMessage() {
    return Component.text("The build limit is: " + this.buildLimit, NamedTextColor.GOLD);
  }

  @Override
  public String getPartyMessage() {
    return "@Side: "
        + ColourConverters.colourToCubeColour(this.teamSide)
        + ColourConverters.colourToCubeColourString(this.teamSide)
        + "&r. Across: "
        + ColourConverters.colourToCubeColour(this.teamAcross)
        + ColourConverters.colourToCubeColourString(this.teamAcross)
        + "&r. Across Side: "
        + ColourConverters.colourToCubeColour(this.teamAcrossSide)
        + ColourConverters.colourToCubeColourString(this.teamAcrossSide)
        + "&r.";
  }

  @Override
  public void setCurrentTeamColour(String teamColour) {
    IndexPair indexPair = this.getIndex(teamColour);

    System.out.println(teamColour);

    this.currentTeamColour = teamColour;
    this.teamSide = this.teamColours.get(indexPair.getSide()).get((indexPair.getLeftRight() + 1) % 2);
    this.teamAcross = this.teamColours.get((indexPair.getSide() + 1) % 2).get((indexPair.getLeftRight() + 1) % 2);
    this.teamAcrossSide = this.teamColours.get((indexPair.getSide() + 1) % 2).get(indexPair.getLeftRight());

    System.out.println(currentTeamColour + teamSide + teamAcross + teamAcrossSide);

    if (indexPair.getLeftRight() == 1) {
      this.currentTeamColour = this.teamSide;
      this.teamSide = teamColour;

      String temp = this.teamAcross;
      this.teamAcross = this.teamAcrossSide;
      this.teamAcrossSide = temp;
    }

    System.out.println(currentTeamColour + teamSide + teamAcross + teamAcrossSide);
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
