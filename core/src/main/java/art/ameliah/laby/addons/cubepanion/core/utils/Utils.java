package art.ameliah.laby.addons.cubepanion.core.utils;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.GameTimerWidget.GameTimerConfig.layoutEnum;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.CrossGameMap;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.DoubleCrossGameMap;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.SquareGameMap;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.TriangleEggWarsMap;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.base.GenLayout;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.base.GenLayout.Location;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.base.LoadedGameMap;
import art.ameliah.laby.addons.cubepanion.core.weave.ChestAPI.ChestLocation;
import art.ameliah.laby.addons.cubepanion.core.weave.GameMapAPI.GameMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.Pair;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Utils {

  private static final Logging LOGGER = Logging.getLogger();


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

  public static @Nullable LoadedGameMap fromAPIMap(GameMap map) {
    CubeGame game = CubeGame.stringToGame(map.game());
    if (game.equals(CubeGame.NONE)) {
      return null;
    }

    switch (map.layout()) {
      case "square" -> {
        return new SquareGameMap(game, map.map_name(), map.team_size(), map.build_limit(),
            twoDeepStringList(transformColours(map)));
      }
      case "cross" -> {
        return new CrossGameMap(game, map.map_name(), map.team_size(), map.build_limit(),
            oneDeepStringList(transformColours(map)));
      }
      case "doublecross" -> {
        return new DoubleCrossGameMap(game, map.map_name(), map.team_size(), map.build_limit(),
            twoDeepStringList(transformColours(map)));
      }
      case "triangle" -> {
        return new TriangleEggWarsMap(game, map.map_name(), map.team_size(), map.build_limit(),
            twoDeepStringList(transformColours(map)));
      }
      default -> {
        LOGGER.error("Unknown map layout", map.layout(), "for map", map.map_name());
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

  static List<String> oneDeepStringList(@NotNull JsonArray array) {
    List<String> list = new ArrayList<>();
    for (JsonElement element : array) {
      list.add(element.getAsString());
    }
    return list;
  }

  static @NotNull JsonArray transformColours(GameMap map) {
    JsonArray array = null;
    try {
      array = (new Gson()).fromJson(map.colours(), JsonArray.class);
    } catch (JsonSyntaxException e) {
      LOGGER.error("Failed to parse colours for " + map.map_name() + " using empty list.", e);
    }
    return array != null ? array : new JsonArray();
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
    return Component.translatable("cubepanion.messages.chests_finder.found", Colours.Success)
        .append(Component.text(String.format("%s, %s, %s", loc.x(), loc.y(), loc.z()),
            NamedTextColor.GRAY));
  }

  public static void handleAPIError(Class<?> origin, Cubepanion addon, Throwable e,
      String msg, String keyError, String key) {
    LOGGER.debug(origin.getCanonicalName() + " " + msg, e);
    if (addon.configuration().getLeaderboardAPIConfig().getErrorInfo().get()) {
      addon.displayMessage(
          Component.translatable(keyError, Component.text(e.getMessage())).color(Colours.Error));
    } else {
      addon.displayMessage(Component.translatable(key).color(Colours.Error));
    }
  }

  public static void timeOutAPIError() {
    Cubepanion.get()
        .displayMessage(Component.translatable("cubepanion.messages.leaderboardAPI.timedOut"));
  }

  public static String getFormattedString(long timeDifference, layoutEnum layout) {
    int seconds = (int) (timeDifference / 1000L);
    int minutes = Math.floorDiv(seconds, 60);
    seconds = seconds - minutes * 60;
    int hours = Math.floorDiv(minutes, 60);
    minutes = minutes - hours * 60;
    int days = Math.floorDiv(hours, 24);
    hours = hours - days * 24;

    switch (layout) {
      case WORDS -> {
        String out = "";
        if (days > 0) {
          out += days + " day" + (days != 1 ? "s " : " ");
        }
        if (hours > 0) {
          out += hours + " hour" + (hours != 1 ? "s " : " ");
        }
        if (minutes > 0) {
          out += minutes + " minute" + (minutes != 1 ? "s " : " ");
        }
        if (seconds > 0) {
          out += seconds + " second" + (seconds != 1 ? "s" : "");
        }
        return out;
      }
      case COLON -> {
        String tail =
            (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
        if (hours > 0) {
          tail = (hours < 10 ? "0" : "") + hours + ":" + tail;
        }
        if (days > 0) {
          tail = days + ":" + tail;
        }
        return tail;
      }
      case SECONDS -> {
        return String.valueOf(seconds);
      }
      case WORDS_SHORT -> {
        String out = "";
        if (days > 0) {
          out += days + "d";
        }
        if (hours > 0) {
          out += hours + "h";
        }
        if (minutes > 0) {
          out += minutes + "m";
        }
        if (seconds > 0) {
          out += seconds + "s";
        }
        return out;
      }
    }
    return "";
  }

  public static Component join(Component delimiter, List<TextComponent> elements) {
    if (elements.isEmpty()) {
      return Component.empty();
    }

    Component out = elements.get(0);
    for (int i = 1; i < elements.size(); i++) {
      Component element = elements.get(i);
      out = out
          .append(delimiter)
          .append(element);
    }
    return out;

  }

}
