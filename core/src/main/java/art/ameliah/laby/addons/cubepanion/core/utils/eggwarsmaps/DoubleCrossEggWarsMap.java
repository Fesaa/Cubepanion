package art.ameliah.laby.addons.cubepanion.core.utils.eggwarsmaps;

import static art.ameliah.laby.addons.cubepanion.core.utils.Utils.getDoubleIndex;

import java.util.List;
import net.labymod.api.client.component.Component;
import art.ameliah.laby.addons.cubepanion.core.utils.eggwarsmaps.base.GenLayout;
import art.ameliah.laby.addons.cubepanion.core.utils.eggwarsmaps.base.LoadedEggWarsMap;

public class DoubleCrossEggWarsMap extends LoadedEggWarsMap {

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
