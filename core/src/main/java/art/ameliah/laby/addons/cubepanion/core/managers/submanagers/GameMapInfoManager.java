package art.ameliah.laby.addons.cubepanion.core.managers.submanagers;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.GameMapInfoSubConfig;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.I18nNamespaces;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.base.LoadedGameMap;
import art.ameliah.laby.addons.cubepanion.core.weave.GameMapAPI;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;

public class GameMapInfoManager {

  private final Cubepanion addon;
  private final GameMapInfoSubConfig gameMapInfoSubConfig;

  private final String mainKey = I18nNamespaces.managerNameSpace + "GameMapInfoManager.";


  public GameMapInfoManager(Cubepanion addon) {
    this.addon = addon;
    this.gameMapInfoSubConfig = addon.configuration().getGameMapInfoSubConfig();
  }

  private void displayGameMapLayout(LoadedGameMap map) {
    ChatExecutor chat = this.addon.labyAPI().minecraft().chatExecutor();

    if (addon.getManager().getMapName().equals(map.getName())) {
      map.setCurrentTeamColour(this.addon.getManager().getTeamColour());
    }

    Component display = this.addon.prefix()
        .append(Component.translatable(this.mainKey + "title", Component.text(map.getName()))
            .color(Colours.Title));

    Component mapLayout = map.getMapLayoutComponent();
    if (mapLayout != null && this.gameMapInfoSubConfig.getMapLayout().get()) {
      display = display
          .append(Component.newline())
          .append(Component.translatable(this.mainKey + "mapLayout", Colours.Primary))
          .append(Component.newline())
          .append(mapLayout);
    }

    Component buildLimit = map.getBuildLimitMessage();
    if (buildLimit != null && this.gameMapInfoSubConfig.getBuildLimit().get()) {
      display = display
          .append(Component.newline())
          .append(Component.newline())
          .append(buildLimit);
    }

    chat.displayClientMessage(display.append(Component.newline()));
  }

  public boolean doGameMapLayout(String mapName) {
    LoadedGameMap map = GameMapAPI.getInstance().getGameMapFromCache(this.addon.getManager().getDivision(), mapName);
    GameMapInfoSubConfig config = this.addon.configuration().getGameMapInfoSubConfig();
    if (map == null) {
      return false;
    }
    this.displayGameMapLayout(map);
    return true;
  }

  public void doGameMapLayout() {
    GameMapInfoSubConfig subConfig = this.addon.configuration().getGameMapInfoSubConfig();
    if (!subConfig.isEnabled().get()) {
      return;
    }
    LoadedGameMap map = GameMapAPI.getInstance().getCurrentMap();
    if (map == null) {
      return;
    }

    this.displayGameMapLayout(map);
  }


}
