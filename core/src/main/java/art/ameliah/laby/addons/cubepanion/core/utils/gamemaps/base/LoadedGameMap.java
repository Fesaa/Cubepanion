package art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.base;

import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.I18nNamespaces;
import net.labymod.api.client.component.Component;
import org.jetbrains.annotations.Nullable;

public abstract class LoadedGameMap {

  protected final Component teamFillerSpaces = spaceMaker(6);
  protected final Component sideSpaces = spaceMaker(2);
  protected final Component betweenSpaces = spaceMaker(4);
  protected final CubeGame game;
  protected final String mapName;
  protected final int teamSize;
  protected final int buildLimit;
  protected String currentTeamColour = "";

  public LoadedGameMap(CubeGame game, String mapName, int teamSize, int buildLimit) {
    this.game = game;
    this.mapName = mapName;
    this.teamSize = teamSize;
    this.buildLimit = buildLimit;
  }

  protected static Component spaceMaker(int n) {
    return Component.text(" ".repeat(Math.max(0, n)));
  }

  public int getBuildLimit() {
    return buildLimit;
  }

  public abstract Component getMapLayoutComponent();

  public Component getBuildLimitMessage() {
    return Component.translatable(
            I18nNamespaces.managerNameSpace + "GameInfoManager.buildLimit", Colours.Primary)
        .append(Component.text(this.buildLimit, Colours.Secondary));
  }

  public String getName() {
    return this.mapName;
  }

  public abstract void setCurrentTeamColour(String teamColour);

  public Component getTeamFiller(String colour) {
    return Component.text("||||||", Colours.colourToNamedTextColor(colour));
  }

  public CubeGame getGame() {
    return game;
  }
}
