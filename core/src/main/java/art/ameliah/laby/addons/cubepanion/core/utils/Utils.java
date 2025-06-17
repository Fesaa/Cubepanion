package art.ameliah.laby.addons.cubepanion.core.utils;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.external.ChestLocation;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.GameTimerWidget.GameTimerConfig.layoutEnum;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.util.Pair;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;

public class Utils {

  private static final Logging log = Logging.create(Cubepanion.class.getSimpleName());

  public static <T> Function<T, CompletableFuture<T>> delay(long delay, TimeUnit unit) {
    return (t) -> CompletableFuture.supplyAsync(() -> t, CompletableFuture.delayedExecutor(delay, unit));
  }

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

  public static Component chestLocationsComponent(ChestLocation loc) {
    return Component.translatable("cubepanion.messages.chests_finder.found", Colours.Success)
        .append(Component.text(String.format("%s, %s, %s", loc.x(), loc.y(), loc.z()),
            NamedTextColor.GRAY));
  }

  public static void handleAPIError(Class<?> origin, Cubepanion addon, Throwable e,
      String msg, String keyError, String key) {
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
