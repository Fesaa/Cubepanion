package org.cubepanion.core.managers.submanagers;

import static org.cubepanion.core.utils.Utils.fromAPIMap;

import art.ameliah.libs.weave.EggWarsMapAPI;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.EggWarsMapInfoSubConfig;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;
import org.cubepanion.core.utils.LOGGER;
import org.cubepanion.core.utils.eggwarsmaps.base.EggWarsMap;
import org.jetbrains.annotations.Nullable;

public class EggWarsMapInfoManager {

  private final Cubepanion addon;
  private final EggWarsMapInfoSubConfig eggWarsMapInfoSubConfig;

  private final String mainKey = I18nNamespaces.managerNameSpace + "EggWarsMapInfoManager.";

  private final HashMap<String, EggWarsMap> eggWarsMapLayouts;

  public EggWarsMapInfoManager(Cubepanion addon) {
    this.addon = addon;
    this.eggWarsMapInfoSubConfig = addon.configuration().getEggWarsMapInfoSubConfig();

    this.eggWarsMapLayouts = new HashMap<>();
  }

  public void queryMaps() {

    try {
      EggWarsMapAPI.EggWarsMap[] eggWarsMaps = Cubepanion.weave.getEggWarsMapAPI()
          .getAllEggWarsMaps()
          .exceptionally(throwable -> {
            LOGGER.error(getClass(), throwable, "Could not update EggWarsMapInfoManager#eggWarsMapLayouts");
            return new EggWarsMapAPI.EggWarsMap[0];
          })
          .get(500, TimeUnit.MILLISECONDS);

      for (EggWarsMapAPI.EggWarsMap map : eggWarsMaps) {
        EggWarsMap eggWarsMap = fromAPIMap(map);
        if (eggWarsMap != null) {
          this.eggWarsMapLayouts.put(map.map_name(), eggWarsMap);
        } else {
          LOGGER.warn(getClass(), "Could not convert EggWars map: " + map.map_name());
        }
      }
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      LOGGER.error(getClass(), e, "EggWarsMapInfoManager#queryMaps took longer than 500ms");
    }
  }


  private void displayEggWarsMapLayout(EggWarsMap map, boolean genLayout) {
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
    EggWarsMap map = this.eggWarsMapLayouts.get(mapName.toLowerCase());
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
    EggWarsMap map = this.eggWarsMapLayouts.get(this.addon.getManager().getMapName().toLowerCase());
    if (map == null) {
      return;
    }

    this.displayEggWarsMapLayout(map, false);
  }

  public @Nullable EggWarsMap getEggWarsMap(String name) {
    return eggWarsMapLayouts.get(name.toLowerCase());
  }
}
