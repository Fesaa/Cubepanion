package art.ameliah.laby.addons.cubepanion.core.managers.submanagers;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.EggWarsMapInfoSubConfig;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.I18nNamespaces;
import art.ameliah.laby.addons.cubepanion.core.utils.eggwarsmaps.base.LoadedEggWarsMap;
import art.ameliah.laby.addons.cubepanion.core.weave.EggWarsMapAPI;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;

public class EggWarsMapInfoManager {

  private final Cubepanion addon;
  private final EggWarsMapInfoSubConfig eggWarsMapInfoSubConfig;

  private final String mainKey = I18nNamespaces.managerNameSpace + "EggWarsMapInfoManager.";


  public EggWarsMapInfoManager(Cubepanion addon) {
    this.addon = addon;
    this.eggWarsMapInfoSubConfig = addon.configuration().getEggWarsMapInfoSubConfig();
  }

  private void displayEggWarsMapLayout(LoadedEggWarsMap map, boolean genLayout) {
    ChatExecutor chat = this.addon.labyAPI().minecraft().chatExecutor();

    if (addon.getManager().getMapName().equals(map.getName())) {
      map.setCurrentTeamColour(this.addon.getManager().getTeamColour());
    }

    Component display = this.addon.prefix()
        .append(Component.translatable(this.mainKey + "title", Component.text(map.getName()))
            .color(Colours.Title));

    Component mapLayout = map.getMapLayoutComponent();
    if (mapLayout != null && this.eggWarsMapInfoSubConfig.getMapLayout().get()) {
      display = display
          .append(Component.newline())
          .append(Component.translatable(this.mainKey + "mapLayout", Colours.Primary))
          .append(Component.newline())
          .append(mapLayout);
    }

    Component buildLimit = map.getBuildLimitMessage();
    if (buildLimit != null && this.eggWarsMapInfoSubConfig.getBuildLimit().get()) {
      display = display
          .append(Component.newline())
          .append(Component.newline())
          .append(buildLimit);
    }

    if (genLayout) {
      display = display
          .append(Component.newline())
          .append(this.addon.prefix())
          .append(map.getGenLayoutComponent());
    }

    chat.displayClientMessage(display.append(Component.newline()));
  }

  public boolean doEggWarsMapLayout(String mapName, boolean keyBind) {
    String name = mapName.toLowerCase();
    LoadedEggWarsMap map = EggWarsMapAPI.getInstance().getEggWarsMapFromCache(name);
    EggWarsMapInfoSubConfig config = this.addon.configuration().getEggWarsMapInfoSubConfig();
    if (map == null) {
      return false;
    }
    this.displayEggWarsMapLayout(map, config.getGenLayout().get() && !keyBind);
    return true;
  }

  public void doEggWarsMapLayout() {
    EggWarsMapInfoSubConfig subConfig = this.addon.configuration().getEggWarsMapInfoSubConfig();
    if (!subConfig.isEnabled().get()) {
      return;
    }
    String name = this.addon.getManager().getMapName().toLowerCase();
    LoadedEggWarsMap map = EggWarsMapAPI.getInstance().getEggWarsMapFromCache(name);
    if (map == null) {
      return;
    }

    this.displayEggWarsMapLayout(map, false);
  }


}
