package org.cubepanion.core.managers.submanagers;

import static org.cubepanion.core.utils.Utils.fromAPIMap;

import art.ameliah.libs.weave.EggWarsMapAPI;
import art.ameliah.libs.weave.Result;
import art.ameliah.libs.weave.WeaveException;
import java.util.HashMap;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.EggWarsMapInfoSubConfig;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;
import org.cubepanion.core.utils.LOGGER;
import org.cubepanion.core.utils.eggwarsmaps.base.EggWarsMap;

public class EggWarsMapInfoManager {

  private final Cubepanion addon;
  private final EggWarsMapInfoSubConfig eggWarsMapInfoSubConfig;

  private final String mainKey = I18nNamespaces.managerNameSpace + "EggWarsMapInfoManager.";

  private final HashMap<String, EggWarsMap> eggWarsMapLayouts;

  public EggWarsMapInfoManager(Cubepanion addon) {
    this.addon = addon;
    this.eggWarsMapInfoSubConfig = addon.configuration().getEggWarsMapInfoSubConfig();

    this.eggWarsMapLayouts = new HashMap<>();
    Result<EggWarsMapAPI.EggWarsMap[], WeaveException> result = Cubepanion.weave.getEggWarsMapAPI()
        .getAllEggWarsMaps();
    if (result.isErr()) {
      LOGGER.warn(getClass(), result.getError(), "Could not fetch EggWars maps from weave: ");
      return;
    }
    EggWarsMapAPI.EggWarsMap[] maps = result.getValue();
    for (EggWarsMapAPI.EggWarsMap map : maps) {
      EggWarsMap eggWarsMap = fromAPIMap(map);
      if (eggWarsMap != null) {
        this.eggWarsMapLayouts.put(map.uniqueName(), eggWarsMap);
      } else {
        LOGGER.warn(getClass(), "Could not convert EggWars map: " + map.uniqueName());
      }
    }
  }


  private void displayEggWarsMapLayout(EggWarsMap map, boolean genLayout, boolean party) {
    ChatExecutor chat = this.addon.labyAPI().minecraft().chatExecutor();

    map.setCurrentTeamColour(this.addon.getManager().getTeamColour());

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

    if (party) {
      String partyMessage = map.getPartyMessage();
      if (partyMessage != null) {
        chat.chat(partyMessage, false);
      }
    }
  }

  public boolean doEggWarsMapLayout(String mapName, boolean keyBind) {
    EggWarsMap map = this.eggWarsMapLayouts.get(mapName.toLowerCase());
    EggWarsMapInfoSubConfig config = this.addon.configuration().getEggWarsMapInfoSubConfig();
    if (map == null) {
      return false;
    }
    this.displayEggWarsMapLayout(map, config.getGenLayout().get() && !keyBind, false);
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

    this.displayEggWarsMapLayout(map, false,
        subConfig.getLogInParty().get() && this.addon.getManager().getPartyManager().isInParty());
  }
}
