package org.cubepanion.core.utils.eggwarsmaps.base;

import net.labymod.api.client.component.Component;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;

public abstract class EggWarsMap {

  protected final String mainKey =
      I18nNamespaces.managerNameSpace + "EggWarsMapInfoManager.directions";

  protected final Component teamFillerSpaces = spaceMaker(6);
  protected final Component sideSpaces = spaceMaker(2);
  protected final Component betweenSpaces = spaceMaker(4);

  protected String currentTeamColour = "";
  protected final String mapName;
  protected final int teamSize;
  protected final int buildLimit;
  protected final GenLayout genLayout;

  public EggWarsMap(String mapName, int teamSize, int buildLimit, GenLayout genLayout) {
    this.mapName = mapName;
    this.teamSize = teamSize;
    this.buildLimit = buildLimit;
    this.genLayout = genLayout;
  }

  public Component getGenLayoutComponent() {
    return this.genLayout.getLayoutComponent();
  }

  public abstract Component getMapLayoutComponent();

  public Component getBuildLimitMessage() {
    return Component.translatable(
            I18nNamespaces.managerNameSpace + "EggWarsMapInfoManager.buildLimit", Colours.Primary)
        .append(Component.text(this.buildLimit, Colours.Secondary));
  }

  public String getName() {
    return this.mapName;
  }

  public abstract void setCurrentTeamColour(String teamColour);

  public Component getTeamFiller(String colour) {
    return Component.text("||||||", Colours.colourToNamedTextColor(colour));
  }

  protected static Component spaceMaker(int n) {
    return Component.text(" ".repeat(Math.max(0, n)));
  }
}
