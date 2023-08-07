package org.cubepanion.core.utils;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.util.logging.Logging;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.managers.CubepanionManager;

public class LOGGER {

  private static Logging log = null;
  private static CubepanionManager manager = null;
  private static boolean enabled = false;

  public static void init() {
    log = Cubepanion.get().logger();
    manager = Cubepanion.get().getManager();
  }

  public static void setEnabled(boolean enabled1) {
    enabled = enabled1;
  }

  public static void setDebug(boolean debug) {
    log.setDebugEnabled(debug);
  }

  private static <T> String formatClass(Class<T> origin) {
    return "[" + origin.getSimpleName() + "@" + origin.getPackageName() + "] ";
  }

  private static String getGameInfo() {
    String out = "\nDivision: " + manager.getDivision() + ", "
        + "Map: " + manager.getMapName();

    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player != null) {
      out += ", Location: " + player.position()
          + ", MainHand: " + player.getMainHandItemStack()
          + ", OffHand: " + player.getOffHandItemStack();
    }

    return out;
  }

  private static <T> String join(T[] msgs) {
    StringBuilder out = new StringBuilder();
    for (T msg : msgs) {
      out.append(" ").append(msg.toString());
    }
    return out.toString().strip();
  }

  @SafeVarargs
  public static <T, V> void info(Class<T> origin, V... msgs) {
    if (enabled) {
      log.info(formatClass(origin) + join(msgs));
    }
  }

  @SafeVarargs
  public static <T, V> void info(Class<T> origin, Throwable cause, V... msgs) {
    if (enabled) {
      log.info(formatClass(origin) + join(msgs), cause);
    }
  }

  @SafeVarargs
  public static <T, V> void info(boolean gameInfo, Class<T> origin, V... msgs) {
    if (enabled) {
      log.info(formatClass(origin) + join(msgs) + (gameInfo ? getGameInfo() : ""));
    }
  }

  @SafeVarargs
  public static <T, V> void warn(Class<T> origin, V... msgs) {
    if (enabled) {
      log.warn(formatClass(origin) + join(msgs));
    }
  }

  @SafeVarargs
  public static <T, V> void warn(Class<T> origin, Throwable cause, V... msgs) {
    if (enabled) {
      log.warn(formatClass(origin) + join(msgs), cause);
    }
  }

  @SafeVarargs
  public static <T, V> void warn(boolean gameInfo, Class<T> origin, V... msgs) {
    if (enabled) {
      log.warn(formatClass(origin) + join(msgs) + (gameInfo ? getGameInfo() : ""));
    }
  }

  @SafeVarargs
  public static <T, V> void debug(Class<T> origin, V... msgs) {
    if (enabled) {
      log.debug(formatClass(origin) + join(msgs));
    }
  }

  @SafeVarargs
  public static <T, V> void debug(Class<T> origin, Throwable cause, V... msgs) {
    if (enabled) {
      log.debug(formatClass(origin) + join(msgs), cause);
    }
  }

  @SafeVarargs
  public static <T, V> void debug(boolean gameInfo, Class<T> origin, V... msgs) {
    if (enabled) {
      log.debug(formatClass(origin) + join(msgs) + (gameInfo ? getGameInfo() : ""));
    }
  }

  @SafeVarargs
  public static <T, V> void error(Class<T> origin, V... msgs) {
    if (enabled) {
      log.error(formatClass(origin) + join(msgs));
    }
  }

  @SafeVarargs
  public static <T, V> void error(Class<T> origin, Throwable cause, V... msgs) {
    if (enabled) {
      log.error(formatClass(origin) + join(msgs), cause);
    }
  }

  @SafeVarargs
  public static <T, V> void error(boolean gameInfo, Class<T> origin, V... msgs) {
    if (enabled) {
      log.error(formatClass(origin) + join(msgs) + (gameInfo ? getGameInfo() : ""));
    }
  }

}
