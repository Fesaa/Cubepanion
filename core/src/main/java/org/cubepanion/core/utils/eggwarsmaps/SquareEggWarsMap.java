package org.cubepanion.core.utils.eggwarsmaps;

import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.I18n;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;
import org.cubepanion.core.utils.eggwarsmaps.base.EggWarsMap;
import org.cubepanion.core.utils.eggwarsmaps.base.GenLayout;

public class SquareEggWarsMap implements EggWarsMap {

  public final String mainKey =
      I18nNamespaces.managerNameSpace + "EggWarsMapInfoManager.directions";
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

  public SquareEggWarsMap(String mapName, int teamSize, int buildLimit, GenLayout genLayout,
      List<List<String>> teamColours) {
    this.mapName = mapName;
    this.teamSize = teamSize;
    this.buildLimit = buildLimit;
    this.genLayout = genLayout;
    this.teamColours = teamColours;
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
        + I18n.translate(this.mainKey + "across")
        + Colours.colourToCubeColour(this.teamAcross)
        + Colours.colourToCubeColourString(this.teamAcross)
        + "&r. "
        + I18n.translate(this.mainKey + "across_side")
        + Colours.colourToCubeColour(this.teamAcrossSide)
        + Colours.colourToCubeColourString(this.teamAcrossSide)
        + "&r.";
  }

  @Override
  public void setCurrentTeamColour(String teamColour) {
    IndexPair indexPair = this.getIndex(teamColour);

    if (indexPair.side() == -1 || indexPair.leftRight() == -1) {
      return;
    }

    this.currentTeamColour = teamColour;
    this.teamSide = this.teamColours.get(indexPair.side()).get((indexPair.leftRight() + 1) % 2);
    this.teamAcross = this.teamColours.get((indexPair.side() + 1) % 2)
        .get((indexPair.leftRight() + 1) % 2);
    this.teamAcrossSide = this.teamColours.get((indexPair.side() + 1) % 2)
        .get(indexPair.leftRight());

    if (indexPair.leftRight() == 1) {
      this.currentTeamColour = this.teamSide;
      this.teamSide = teamColour;

      String temp = this.teamAcross;
      this.teamAcross = this.teamAcrossSide;
      this.teamAcrossSide = temp;
    }
  }

  IndexPair getIndex(String member) {

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

  record IndexPair(int leftRight, int side) {

  }
}
