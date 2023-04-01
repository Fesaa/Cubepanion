package org.cubecraftutilities.core.managers.submanagers;

import java.util.Arrays;
import java.util.HashMap;
import net.labymod.api.client.chat.ChatExecutor;
import net.labymod.api.client.component.Component;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.subconfig.EggWarsMapInfoSubConfig;
import org.cubecraftutilities.core.utils.Colours;
import org.cubecraftutilities.core.utils.eggwarsmaps.CrossEggWarsMap;
import org.cubecraftutilities.core.utils.eggwarsmaps.DoubleCrossEggWarsMap;
import org.cubecraftutilities.core.utils.eggwarsmaps.SquareEggWarsMap;
import org.cubecraftutilities.core.utils.eggwarsmaps.base.EggWarsMap;
import org.cubecraftutilities.core.utils.eggwarsmaps.base.GenLayout;
import org.cubecraftutilities.core.utils.eggwarsmaps.base.GenLayout.Generator;
import org.cubecraftutilities.core.utils.eggwarsmaps.base.GenLayout.Location;
import org.cubecraftutilities.core.utils.eggwarsmaps.base.GenLayout.MapGenerator;

public class EggWarsMapInfoManager {

  private final CCU addon;
  private final EggWarsMapInfoSubConfig eggWarsMapInfoSubConfig;

  private final HashMap<String, EggWarsMap> eggWarsMapLayouts;

  public EggWarsMapInfoManager(CCU addon) {
    this.addon = addon;
    this.eggWarsMapInfoSubConfig = addon.configuration().getEggWarsMapInfoSubConfig();

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

    GenLayout AquaticGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 1, 4),
            new MapGenerator(Generator.GOLD, Location.SEMI_MIDDLE, 2, 8),
            new MapGenerator(Generator.IRON, Location.SEMI_MIDDLE, 3, 8),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );

    this.eggWarsMapLayouts.put("Aquatic", new CrossEggWarsMap("Aquatic", 4, 89, AquaticGenLayout, "light_purple", "red", "yellow", "blue"));

    GenLayout CyberSnowGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.SEMI_MIDDLE, 1, 8),
            new MapGenerator(Generator.GOLD, Location.SEMI_MIDDLE, 2, 8),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Cyber Snow", new DoubleCrossEggWarsMap("Cyber Snow", 2, 77, CyberSnowGenLayout,
        Arrays.asList("dark_blue", "red"), Arrays.asList("gray", "light_purple"), Arrays.asList("dark_gray", "dark_aqua"), Arrays.asList("yellow", "green")));


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
    this.eggWarsMapLayouts.put("Mushroom", new SquareEggWarsMap("Mushroom", 4, 72, MushroomGenLayout, Arrays.asList("green", "red"), Arrays.asList("yellow", "aqua")));

    GenLayout ModernGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 2, 2),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 3, 2),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 0, 1)
        );
    this.eggWarsMapLayouts.put("Modern", new SquareEggWarsMap("Modern", 2, 54, ModernGenLayout, Arrays.asList("red", "yellow"), Arrays.asList("light_purple", "aqua")));

    GenLayout CyberCityGenLayout =
        new GenLayout(new MapGenerator(Generator.DIAMOND, Location.MIDDLE, 1, 3),
            new MapGenerator(Generator.GOLD, Location.MIDDLE, 2, 6),
            new MapGenerator(Generator.IRON, Location.MIDDLE, 2, 6),
            new MapGenerator(Generator.DIAMOND, Location.BASE, 0, 1),
            new MapGenerator(Generator.GOLD, Location.BASE, 1, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 2, 1),
            new MapGenerator(Generator.IRON, Location.BASE, 1, 1)
        );
    this.eggWarsMapLayouts.put("Cyber City", new SquareEggWarsMap("Cyber City", 2, 67, CyberCityGenLayout, Arrays.asList("light_purple", "dark_aqua"), Arrays.asList("red", "green")));
  }

  private void displayEggWarsMapLayout(EggWarsMap map, boolean genLayout, boolean party) {
    ChatExecutor chat = this.addon.labyAPI().minecraft().chatExecutor();

    map.setCurrentTeamColour(this.addon.getManager().getTeamColour());

    Component display = this.addon.prefix()
        .append(Component.text("------- Map Info For " + map.getName() + " -------", Colours.Title));

    Component mapLayout = map.getMapLayoutComponent();
    if (mapLayout != null && this.eggWarsMapInfoSubConfig.getMapLayout().get()) {
      display = display
          .append(Component.newline())
          .append(Component.text("Map Layout;", Colours.Primary))
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
    EggWarsMap map = this.eggWarsMapLayouts.get(mapName);
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
    EggWarsMap map = this.eggWarsMapLayouts.get(this.addon.getManager().getMapName());
    if (map == null) {
      return;
    }

    this.displayEggWarsMapLayout(map, false, subConfig.getLogInParty().get() && this.addon.getManager().getPartyManager().isInParty());
  }
}
