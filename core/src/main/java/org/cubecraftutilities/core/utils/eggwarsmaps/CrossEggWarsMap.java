package org.cubecraftutilities.core.utils.eggwarsmaps;

import java.util.Arrays;
import java.util.List;
import net.labymod.api.client.component.Component;
import org.cubecraftutilities.core.utils.Colours;
import org.cubecraftutilities.core.utils.eggwarsmaps.base.EggWarsMap;
import org.cubecraftutilities.core.utils.eggwarsmaps.base.GenLayout;

public class CrossEggWarsMap implements EggWarsMap {

  private final Component teamFillerSpaces = Component.text("      ");
  private final Component sideSpaces = Component.text("  ");
  private final Component betweenSpaces = Component.text("    ");

  private final String mapName;
  private final int teamSize;
  private final int buildLimit;
  private final GenLayout genLayout;

  // Team Colour in cyclic order, moving left
  private final List<String> teamColours;

  private String currentTeamColour = "";
  private String teamLeft = "";
  private String teamRight = "";
  private String teamBefore = "";

  public CrossEggWarsMap(String mapName, int teamSize, int buildLimit, GenLayout genLayout,  String... teamColours) {
    this.mapName = mapName;
    this.teamSize = teamSize;
    this.buildLimit = buildLimit;
    this.genLayout = genLayout;
    this.teamColours = Arrays.asList(teamColours);
    this.setCurrentTeamColour(this.teamColours.get(0));
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
  public Component getBuildLimitMessage() {
    return Component.text("Build limit: ", Colours.Primary)
        .append(Component.text(this.buildLimit, Colours.Secondary));
  }

  @Override
  public String getPartyMessage() {
    return "@Left: "
        + Colours.colourToCubeColour(this.teamLeft)
        + Colours.colourToCubeColourString(this.teamLeft)
        + "&r. Right: "
        + Colours.colourToCubeColour(this.teamRight)
        + Colours.colourToCubeColourString(this.teamRight)
        + "&r. In Front: "
        + Colours.colourToCubeColour(this.teamBefore)
        + Colours.colourToCubeColourString(this.teamBefore)
        + "&r.";
  }

  @Override
  public String getName() {
    return this.mapName;
  }

  @Override
  public void setCurrentTeamColour(String teamColour) {
    int teamIndex = this.getIndex(this.teamColours, teamColour);

    if (teamIndex == -1) {
      return;
    }

    this.currentTeamColour = teamColour;
    this.teamLeft = this.teamColours.get((teamIndex + 1) % 4);
    this.teamBefore = this.teamColours.get((teamIndex + 2) % 4);
    this.teamRight = this.teamColours.get((teamIndex + 3) % 4);
  }

  public int getIndex(List<String> list, String member) {
    int i = 0;
    for (String s : list) {
      if (s.equals(member)) {
        return i;
      }
      i++;
    }
    return -1;
  }
}
