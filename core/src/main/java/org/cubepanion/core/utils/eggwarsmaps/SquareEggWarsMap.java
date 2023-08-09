package org.cubepanion.core.utils.eggwarsmaps;

import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.I18n;
import net.labymod.api.util.Pair;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;
import org.cubepanion.core.utils.Utils;
import org.cubepanion.core.utils.eggwarsmaps.base.EggWarsMap;
import org.cubepanion.core.utils.eggwarsmaps.base.GenLayout;
import org.jetbrains.annotations.NotNull;

import static org.cubepanion.core.utils.Utils.getDoubleIndex;

public class SquareEggWarsMap extends EggWarsMap {

  private final List<List<String>> teamColours;

  private String currentTeamColour = "";
  private String teamSide = "";
  private String teamAcross = "";
  private String teamAcrossSide = "";

  public SquareEggWarsMap(String mapName, int teamSize, int buildLimit, GenLayout genLayout,
      List<List<String>> teamColours) {
    super(mapName, teamSize, buildLimit, genLayout);
    this.teamColours = teamColours;
    this.setCurrentTeamColour(this.teamColours.get(0).get(0));
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
    this.teamAcross = this.teamColours.get((first + 1) % 2).get((second + 1) % 2);
    this.teamAcrossSide = this.teamColours.get((first + 1) % 2) .get(second);

    if (second == 1) {
      this.currentTeamColour = this.teamSide;
      this.teamSide = teamColour;

      String temp = this.teamAcross;
      this.teamAcross = this.teamAcrossSide;
      this.teamAcrossSide = temp;
    }
  }
}
