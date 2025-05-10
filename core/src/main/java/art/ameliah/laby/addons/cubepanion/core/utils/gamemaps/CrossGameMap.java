package art.ameliah.laby.addons.cubepanion.core.utils.gamemaps;

import java.util.List;
import net.labymod.api.client.component.Component;

public class CrossGameMap extends AbstractGameMap {

  // Team Colour in cyclic order, moving left
  private final List<String> teamColours;

  private String teamLeft = "";
  private String teamRight = "";
  private String teamBefore = "";

  public CrossGameMap(String mapName, int teamSize, int buildLimit, List<String> teamColours) {
    super(mapName, teamSize, buildLimit);
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
    int teamIndex = indexOf(teamColour);

    if (teamIndex == -1) {
      return;
    }

    this.currentTeamColour = teamColour;
    this.teamLeft = this.teamColours.get((teamIndex + 1) % 4);
    this.teamBefore = this.teamColours.get((teamIndex + 2) % 4);
    this.teamRight = this.teamColours.get((teamIndex + 3) % 4);
  }

  private int indexOf(String colour) {
    int i = 0;
    for (String teamColour : teamColours) {
      if (teamColour.equals(colour)) {
        return i;
      }
      i++;
    }
    return -1;
  }
}
