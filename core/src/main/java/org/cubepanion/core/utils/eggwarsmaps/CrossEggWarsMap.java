package org.cubepanion.core.utils.eggwarsmaps;

import java.util.List;
import net.labymod.api.client.component.Component;
import org.cubepanion.core.utils.eggwarsmaps.base.EggWarsMap;
import org.cubepanion.core.utils.eggwarsmaps.base.GenLayout;

public class CrossEggWarsMap extends EggWarsMap {

  // Team Colour in cyclic order, moving left
  private final List<String> teamColours;

  private String currentTeamColour = "";
  private String teamLeft = "";
  private String teamRight = "";
  private String teamBefore = "";

  public CrossEggWarsMap(String mapName, int teamSize, int buildLimit, GenLayout genLayout,
      List<String> teamColours) {
    super(mapName, teamSize, buildLimit, genLayout);
    this.teamColours = teamColours;
    this.setCurrentTeamColour(this.teamColours.get(0));
  }

  @Override
  public Component getMapLayoutComponent() {
    return Component.empty()
        .append(this.sideSpaces)
        .append(this.teamFillerSpaces)
        .append(this.betweenSpaces)
        .append(this.getTeamFiller(this.teamBefore))
        .append(Component.newline())
        .append(this.sideSpaces)
        .append(this.getTeamFiller(this.teamLeft))
        .append(this.betweenSpaces)
        .append(this.teamFillerSpaces)
        .append(this.betweenSpaces)
        .append(this.getTeamFiller(this.teamRight))
        .append(Component.newline())
        .append(this.sideSpaces)
        .append(this.teamFillerSpaces)
        .append(this.betweenSpaces)
        .append(this.getTeamFiller(this.currentTeamColour));
  }

  @Override
  public void setCurrentTeamColour(String teamColour) {
    int teamIndex = teamColour.indexOf(teamColour);

    if (teamIndex == -1) {
      return;
    }

    this.currentTeamColour = teamColour;
    this.teamLeft = this.teamColours.get((teamIndex + 1) % 4);
    this.teamBefore = this.teamColours.get((teamIndex + 2) % 4);
    this.teamRight = this.teamColours.get((teamIndex + 3) % 4);
  }
}
