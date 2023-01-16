package org.ccu.core.config.submanagers;

import java.util.HashMap;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.labymod.api.client.chat.ChatExecutor;
import org.ccu.core.CCU;
import org.ccu.core.config.subconfig.EggWarsMapInfoSubConfig;
import org.ccu.core.utils.imp.CrossEggWarsMap;
import org.ccu.core.utils.imp.DoubleCrossEggWarsMap;
import org.ccu.core.utils.imp.SquareEggWarsMap;
import org.ccu.core.utils.imp.base.EggWarsMap;
import org.ccu.core.utils.imp.base.GenLayout;
import org.ccu.core.utils.imp.base.GenLayout.Generator;
import org.ccu.core.utils.imp.base.GenLayout.Location;
import org.ccu.core.utils.imp.base.GenLayout.MapGenerator;

public class EggWarsMapInfoManager {

  private final CCU addon;

  private final HashMap<String, EggWarsMap> eggWarsMapLayouts;

  public EggWarsMapInfoManager(CCU addon) {
    this.addon = addon;

    this.eggWarsMapLayouts = new HashMap<>();
    this.registerEggWarsMaps();
  }

  private void registerEggWarsMaps() {

    GenLayout YukiGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 4),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 2, 4),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Yuki", new CrossEggWarsMap("Yuki", 4, 63, YukiGenLayout,  "yellow", "dark_blue", "green", "red"));

    GenLayout OlympusGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 3),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 2, 3),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 3, 4),
            new MapGenerator(Generator.DIAMOND, Location.SEMI_MIDDLE, 1, 4),
            new MapGenerator(Generator.GOLD, Location.SEMI_MIDDLE, 2, 4),
            new MapGenerator(Generator.IRON, Location.SEMI_MIDDLE, 3, 8),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 2),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 2)
        );
    this.eggWarsMapLayouts.put("Olympus", new CrossEggWarsMap("Olympus", 4, 71, OlympusGenLayout, "green", "dark_aqua", "yellow", "red"));

    GenLayout PalaceGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 3, 2),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 3, 1),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 3, 1),
            new MapGenerator(Generator.DIAMOND, Location.SEMI_MIDDLE, 2, 4),
            new MapGenerator(Generator.GOLD, Location.SEMI_MIDDLE, 2, 8),
            new MapGenerator(Generator.IRON, Location.SEMI_MIDDLE, 2, 8),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 2),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Palace", new CrossEggWarsMap("Palace", 4, 70, PalaceGenLayout, "dark_purple", "yellow", "dark_blue", "red"));

    GenLayout RuinsGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.DIAMOND, Location.SEMI_MIDDLE, 2, 2),
            new MapGenerator(Generator.GOLD, Location.SEMI_MIDDLE, 2, 2),
            new MapGenerator(Generator.IRON, Location.SEMI_MIDDLE, 3, 2),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 2),
            new MapGenerator(Generator.GOLD, Location.BASE, 2, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 2),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Ruins", new CrossEggWarsMap("Ruins", 4, 86, RuinsGenLayout, "dark_purple", "red", "green", "dark_blue"));

    GenLayout BeachGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 4, 2),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 2),
            new MapGenerator(Generator.IRON, Location.BASE, 0, 1)
        );
    this.eggWarsMapLayouts.put("Beach", new CrossEggWarsMap("Beach", 4, 86, BeachGenLayout, "dark_purple", "green", "gold", "aqua"));

    GenLayout FairytaleGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 3, 2),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 3, 4),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 3, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Fairytale", new CrossEggWarsMap("Fairytale", 4, 64, FairytaleGenLayout, "dark_blue", "red", "dark_purple", "yellow"));


    GenLayout CyberSnowGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.SEMI_MIDDLE, 1, 8),
            new MapGenerator(Generator.GOLD, Location.SEMI_MIDDLE, 2, 8),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Cyber Snow", new DoubleCrossEggWarsMap("Cyber Snow", 2, 77, CyberSnowGenLayout,
        List.of("dark_blue", "red"), List.of("gray", "light_purple"), List.of("dark_gray", "dark_aqua"), List.of("yellow", "green")));


    GenLayout MushroomGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 1, 2),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 3, 2),
            new MapGenerator(Generator.DIAMOND, Location.SEMI_MIDDLE, 1, 2),
            new MapGenerator(Generator.IRON, Location.SEMI_MIDDLE, 3, 2),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 3, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Mushroom", new SquareEggWarsMap("Mushroom", 4, 72, MushroomGenLayout, List.of("green", "red"), List.of("yellow", "aqua")));

    GenLayout ModernGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 3, 2),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 0, 1)
        );
    this.eggWarsMapLayouts.put("Modern", new SquareEggWarsMap("Modern", 2, 54, ModernGenLayout, List.of("red", "yellow"), List.of("aqua", "light_purple")));

    GenLayout CyberCityGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 1, 3),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 2, 6),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 2, 6),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Cyber City", new SquareEggWarsMap("Cyber City", 2, 67, CyberCityGenLayout, List.of("light_purple", "dark_aqua"), List.of("red", "green")));

    
  }

  public boolean doEggWarsMapLayout(String mapName, boolean keyBind) {
    
    ChatExecutor chat = this.addon.labyAPI().minecraft().chatExecutor();
    EggWarsMap map = this.eggWarsMapLayouts.get(mapName);
    EggWarsMapInfoSubConfig config = this.addon.configuration().getEggWarsMapInfoSubConfig();
    if (map == null) {
      return false;
    }
    chat.displayClientMessage(Component.text("\nMap Info for " + mapName, NamedTextColor.GOLD));
    map.setCurrentTeamColour(this.addon.getManager().getTeamColour());
    Component mapLayout = map.getMapLayoutComponent();
    if (mapLayout != null) {
      chat.displayClientMessage(mapLayout);
    }
    Component buildLimit = map.getBuildLimitMessage();
    if (buildLimit != null) {
      chat.displayClientMessage(buildLimit);
    }
    if (config.getGenLayout().get() && !keyBind) {
      chat.displayClientMessage(map.getGenLayoutComponent());
    }
    return true;
  }

  public void doEggWarsMapLayout() {
    
    EggWarsMapInfoSubConfig subConfig = this.addon.configuration().getEggWarsMapInfoSubConfig();
    if (!subConfig.isEnabled().get()) {
      return;
    }
    this.setCurrentTeamColour();

    ChatExecutor chat = this.addon.labyAPI().minecraft().chatExecutor();
    chat.displayClientMessage(Component.text("\nMap Info for " + this.addon.getManager().getMapName(), NamedTextColor.GOLD));

    if (subConfig.getMapLayout().get()) {
      Component mapLayout = this.getMapLayoutComponent();
      if (mapLayout != null) {
        chat.displayClientMessage(mapLayout);
      }
    }

    if (subConfig.getBuildLimit().get()) {
      Component buildLimit = this.getBuildLimitMessage();
      if (buildLimit != null) {
        chat.displayClientMessage(buildLimit);
      }
    }

    if (subConfig.getLogInParty().get()) {
      if (this.addon.getManager().getPartyManager().isInParty()) {
        String partyMessage = this.getPartyMessage();
        if (partyMessage != null) {
          chat.chat(partyMessage, false);
        }
      }
    }
  }

  private void setCurrentTeamColour() {
    
    EggWarsMap map = this.eggWarsMapLayouts.get(this.addon.getManager().getMapName());
    if (map != null) {
      map.setCurrentTeamColour(this.addon.getManager().getTeamColour());
    }
  }

  private Component getMapLayoutComponent() {
    
    EggWarsMap map = this.eggWarsMapLayouts.get(this.addon.getManager().getMapName());
    if (map == null) {
      return null;
    }
    return map.getMapLayoutComponent();
  }

  private Component getBuildLimitMessage() {
    
    EggWarsMap map = this.eggWarsMapLayouts.get(this.addon.getManager().getMapName());
    if (map == null) {
      return null;
    }
    return map.getBuildLimitMessage();
  }

  private String getPartyMessage() {
    
    EggWarsMap map = this.eggWarsMapLayouts.get(this.addon.getManager().getMapName());
    if (map == null) {
      return null;
    }
    return map.getPartyMessage();
  }

}
