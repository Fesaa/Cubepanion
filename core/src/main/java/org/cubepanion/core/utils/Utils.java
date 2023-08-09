package org.cubepanion.core.utils;

import art.ameliah.libs.weave.ChestAPI.ChestLocation;
import art.ameliah.libs.weave.EggWarsMapAPI;
import art.ameliah.libs.weave.EggWarsMapAPI.Generator;
import art.ameliah.libs.weave.WeaveException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.Pair;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.eggwarsmaps.CrossEggWarsMap;
import org.cubepanion.core.utils.eggwarsmaps.DoubleCrossEggWarsMap;
import org.cubepanion.core.utils.eggwarsmaps.SquareEggWarsMap;
import org.cubepanion.core.utils.eggwarsmaps.base.EggWarsMap;
import org.cubepanion.core.utils.eggwarsmaps.base.GenLayout;
import org.cubepanion.core.utils.eggwarsmaps.base.GenLayout.Location;
import org.cubepanion.core.utils.eggwarsmaps.base.GenLayout.MapGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Utils {


  public static <T> @NotNull Pair<Integer, Integer> getDoubleIndex(List<List<T>> list, T value) {
    int outer = 0;
    int inner = 0;

    for (List<T> group : list) {
      for (T v : group) {
        if (v.equals(value)) {
          return Pair.of(outer, inner);
        }
        inner++;
      }
      outer++;
      inner = 0;
    }

    return Pair.of(-1, -1);
  }

  public static @Nullable EggWarsMap fromAPIMap(EggWarsMapAPI.EggWarsMap map) {
    Generator[] gens = map.generators();
    List<MapGenerator> mapGenerators = new java.util.ArrayList<>(List.of());
    for (Generator gen : gens) {
      MapGenerator mapGen = new MapGenerator(transformGen(gen.genType()), transformLoc(gen.location()), gen.count(), gen.level());
      mapGenerators.add(mapGen);
    }
    GenLayout layout = new GenLayout(mapGenerators);
    switch (map.layout()) {
      case "square" -> {
        return new SquareEggWarsMap(map.mapName(), map.teamSize(), map.buildLimit(), layout, twoDeepStringList(transformColours(map)));
      }
      case "cross" -> {
        return new CrossEggWarsMap(map.mapName(), map.teamSize(), map.buildLimit(), layout, oneDeepStringList(transformColours(map)));
      }
      case "doublecross" -> {
        return new DoubleCrossEggWarsMap(map.mapName(), map.teamSize(), map.buildLimit(), layout, twoDeepStringList(transformColours(map)));
      }
      default -> {
        LOGGER.error(Utils.class, "Unknown map layout", map.layout(), "for map", map.mapName());
        return null;
      }
    }
  }

  static List<List<String>> twoDeepStringList(JsonArray array) {
    List<List<String>> list = new ArrayList<>(List.of());
    for (JsonElement element : array) {
      List<String> inner = new ArrayList<>(List.of());
      for (JsonElement element2 : element.getAsJsonArray()) {
        inner.add(element2.getAsString());
      }
      list.add(inner);
    }
    return list;
  }

  static List<String> oneDeepStringList(JsonArray array) {
    List<String> list = new ArrayList<>();
    for (JsonElement element : array) {
      list.add(element.getAsString());
    }
    return list;
  }

  static JsonArray transformColours(EggWarsMapAPI.EggWarsMap map) {
    try {
      return (new Gson()).fromJson(map.colour(), JsonArray.class);
    } catch (JsonSyntaxException e) {
      LOGGER.error(Utils.class, e, "Failed to parse colours for", map.mapName(), " using empty list.");
    }
    return new JsonArray();
  }

  static GenLayout.Generator transformGen(String type) {
      return switch (type) {
        case "diamond" -> GenLayout.Generator.DIAMOND;
        case "gold" -> GenLayout.Generator.GOLD;
        default -> GenLayout.Generator.IRON;
      };
  }

  static Location transformLoc(String loc) {
      return switch (loc) {
          case "middle" -> Location.MIDDLE;
          case "semimiddle" -> Location.SEMI_MIDDLE;
          default -> Location.BASE;
      };
  }

  public static Component chestLocationsComponent(ChestLocation loc) {
    return Component.text("Found a chest @ ", NamedTextColor.GREEN)
        .append(Component.text(String.format("%s, %s, %s", loc.x(), loc.y(), loc.z()),
            NamedTextColor.GRAY));
  }

  public static void handleResultError(Class<?> origin, Cubepanion addon, WeaveException e,
      String msg, String keyError, String key) {
    LOGGER.debug(origin, e, msg);
    if (addon.configuration().getLeaderboardAPIConfig().getErrorInfo().get()) {
      addon.displayMessage(
          Component.translatable(keyError, Component.text(e.getMessage())).color(Colours.Error));
    } else {
      addon.displayMessage(Component.translatable(key).color(Colours.Error));
    }
  }

}
