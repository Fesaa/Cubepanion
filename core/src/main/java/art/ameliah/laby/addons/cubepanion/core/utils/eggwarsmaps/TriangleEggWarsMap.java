package art.ameliah.laby.addons.cubepanion.core.utils.eggwarsmaps;

import java.util.List;
import net.labymod.api.client.component.Component;
import art.ameliah.laby.addons.cubepanion.core.utils.Utils;
import art.ameliah.laby.addons.cubepanion.core.utils.eggwarsmaps.base.GenLayout;
import art.ameliah.laby.addons.cubepanion.core.utils.eggwarsmaps.base.LoadedEggWarsMap;

public class TriangleEggWarsMap extends LoadedEggWarsMap {

  private final List<List<String>> teamColours;
  String teamUnderLeft = "";
  String teamUnderRight = "";
  String teamLeftPoint = "";
  String teamRightPoint = "";
  String teamUpLeft = "";
  String teamUpRight = "";
  int triangleLocation = -1;

  public TriangleEggWarsMap(String mapName, int teamSize, int buildLimit, GenLayout genLayout,
      List<List<String>> teamColours) {
    super(mapName, teamSize, buildLimit, genLayout);
    this.teamColours = teamColours;
    this.setCurrentTeamColour(this.teamColours.get(0).get(0));
  }

  @Override
  public Component getMapLayoutComponent() {
    if (triangleLocation != 1) {
      return Component.empty()
          .append(spaceMaker(10)).append(this.getTeamFiller(this.teamUpLeft))
          .append(teamFillerSpaces).append(this.getTeamFiller(this.teamUpRight))
          .append(Component.newline())
          .append(sideSpaces).append(this.getTeamFiller(this.teamLeftPoint))
          .append(spaceMaker(22)).append(this.getTeamFiller(this.teamRightPoint))
          .append(Component.newline())
          .append(spaceMaker(10)).append(this.getTeamFiller(this.teamUnderLeft))
          .append(teamFillerSpaces).append(this.getTeamFiller(this.teamUnderRight));
    }
    return Component.empty()
        .append(spaceMaker(8)).append(this.getTeamFiller(this.teamRightPoint))
        .append(Component.newline())
        .append(sideSpaces).append(this.getTeamFiller(this.teamUpRight))
        .append(spaceMaker(8)).append(this.getTeamFiller(this.teamUnderRight))
        .append(Component.newline()).append(Component.newline())
        .append(sideSpaces).append(this.getTeamFiller(this.teamUpLeft))
        .append(spaceMaker(8)).append(this.getTeamFiller(this.teamUnderLeft))
        .append(Component.newline())
        .append(spaceMaker(8)).append(this.getTeamFiller(this.teamLeftPoint));
  }

  @Override
  public void setCurrentTeamColour(String teamColour) {
    var index = Utils.getDoubleIndex(teamColours, teamColour);

    Integer teamTriangleLocation = index.getFirst();
    Integer teamLeftRight = index.getSecond();
    if (teamLeftRight == null || teamTriangleLocation == null) {
      return;
    }

    switch (teamTriangleLocation) {
      case 0 -> {
        teamUnderLeft = teamColours.get(0).get(0);
        teamUnderRight = teamColours.get(0).get(1);
        teamLeftPoint = teamColours.get(1).get(0);
        teamRightPoint = teamColours.get(1).get(1);
        teamUpLeft = teamColours.get(2).get(1);
        teamUpRight = teamColours.get(2).get(0);
      }
      case 1 -> {
        if (teamLeftRight == 0) {
          teamUnderLeft = teamColours.get(0).get(0);
          teamUnderRight = teamColours.get(0).get(1);
          teamLeftPoint = teamColours.get(1).get(0);
          teamRightPoint = teamColours.get(1).get(1);
          teamUpLeft = teamColours.get(2).get(1);
          teamUpRight = teamColours.get(2).get(0);
        } else {
          teamUnderLeft = teamColours.get(2).get(0);
          teamUnderRight = teamColours.get(2).get(1);
          teamLeftPoint = teamColours.get(1).get(1);
          teamRightPoint = teamColours.get(1).get(0);
          teamUpLeft = teamColours.get(0).get(1);
          teamUpRight = teamColours.get(0).get(0);
        }
      }
      case 2 -> {
        teamUnderLeft = teamColours.get(2).get(0);
        teamUnderRight = teamColours.get(2).get(1);
        teamLeftPoint = teamColours.get(1).get(1);
        teamRightPoint = teamColours.get(1).get(0);
        teamUpLeft = teamColours.get(0).get(1);
        teamUpRight = teamColours.get(0).get(0);
      }
    }
    triangleLocation = teamTriangleLocation;
  }
}
