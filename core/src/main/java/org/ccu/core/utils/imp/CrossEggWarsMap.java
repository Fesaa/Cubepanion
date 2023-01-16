package org.ccu.core.utils.imp;

import java.util.Arrays;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.ccu.core.Colours;
import org.ccu.core.utils.ColourConverters;
import org.ccu.core.utils.imp.base.EggWarsMap;
import org.ccu.core.utils.imp.base.GenLayout;

public class CrossEggWarsMap implements EggWarsMap {

  private final Component teamFiller = Component.text("||||||");
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
        .append(this.teamFiller.color(ColourConverters.colourToNamedTextColor(this.teamBefore)))
        .append(Component.newline())
        .append(this.sideSpaces)
        .append(this.teamFiller.color(ColourConverters.colourToNamedTextColor(this.teamLeft)))
        .append(this.betweenSpaces)
        .append(this.teamFillerSpaces)
        .append(this.betweenSpaces)
        .append(this.teamFiller.color(ColourConverters.colourToNamedTextColor(this.teamRight)))
        .append(Component.newline())
        .append(this.sideSpaces)
        .append(this.teamFillerSpaces)
        .append(this.betweenSpaces)
        .append(this.teamFiller.color(ColourConverters.colourToNamedTextColor(this.currentTeamColour)));
  }

  @Override
  public Component getBuildLimitMessage() {
    return Component.text("Build limit: ", Colours.Primary)
        .append(Component.text(this.buildLimit, Colours.Secondary));
  }

  @Override
  public String getPartyMessage() {
    return "@Left: "
        + ColourConverters.colourToCubeColour(this.teamLeft)
        + ColourConverters.colourToCubeColourString(this.teamLeft)
        + "&r. Right: "
        + ColourConverters.colourToCubeColour(this.teamRight)
        + ColourConverters.colourToCubeColourString(this.teamRight)
        + "&r. In Front: "
        + ColourConverters.colourToCubeColour(this.teamBefore)
        + ColourConverters.colourToCubeColourString(this.teamBefore)
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
