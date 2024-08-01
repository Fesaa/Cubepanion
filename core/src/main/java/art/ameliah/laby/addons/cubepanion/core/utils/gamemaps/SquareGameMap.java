package art.ameliah.laby.addons.cubepanion.core.utils.gamemaps;

import static art.ameliah.laby.addons.cubepanion.core.utils.Utils.getDoubleIndex;

import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.base.LoadedGameMap;
import java.util.List;
import net.labymod.api.client.component.Component;

public class SquareGameMap extends LoadedGameMap {

  private final List<List<String>> teamColours;

  private String teamSide = "";
  private String teamAcross = "";
  private String teamAcrossSide = "";

  public SquareGameMap(CubeGame game,String mapName, int teamSize, int buildLimit, List<List<String>> teamColours) {
    super(game, mapName, teamSize, buildLimit);
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
    this.teamAcrossSide = this.teamColours.get((first + 1) % 2).get(second);

    if (second == 1) {
      this.currentTeamColour = this.teamSide;
      this.teamSide = teamColour;

      String temp = this.teamAcross;
      this.teamAcross = this.teamAcrossSide;
      this.teamAcrossSide = temp;
    }
  }
}
