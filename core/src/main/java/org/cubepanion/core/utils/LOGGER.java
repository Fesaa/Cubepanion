package org.cubepanion.core.utils;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.util.logging.Logging;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.managers.CubepanionManager;
import org.jetbrains.annotations.NotNull;

public class LOGGER {

  private static Logging log = null;
  private static CubepanionManager manager = null;

  public static void setLog(Logging logging) {
    log = logging;
  }

  public static void setManager(CubepanionManager m) {
    manager = m;
  }

  private static <T> String formatClass(Class<T> origin) {
    return "[" + origin.getSimpleName() + "@" + origin.getPackageName() + "] ";
  }

  private static @NotNull String getGameInfo() {
    if (manager == null) {
      return "GameInfo{}";
    }
    String out = "\nGameInfo{Division: " + manager.getDivision()
        + ", Map: " + manager.getMapName()
        + ", Alive: " + !manager.isEliminated();

    if (manager.getDivision().equals(CubeGame.TEAM_EGGWARS)) {
      out += ", Team Colour: " + manager.getTeamColour();
    }

    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player != null) {
      out += ", PlayerInfo{Location: " + player.position()
          + ", MainHand: " + player.getMainHandItemStack()
          + ", OffHand: " + player.getOffHandItemStack()
          + "}";
    }

    return out + "}";
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
    log.info(formatClass(origin) + join(msgs));
  }

  @SafeVarargs
  public static <T, V> void info(Class<T> origin, Throwable cause, V... msgs) {
    log.info(formatClass(origin) + join(msgs), cause);
  }

  @SafeVarargs
  public static <T, V> void info(boolean gameInfo, Class<T> origin, V... msgs) {
    log.info(formatClass(origin) + join(msgs) + (gameInfo ? getGameInfo() : ""));
  }

  @SafeVarargs
  public static <T, V> void warn(Class<T> origin, V... msgs) {
    log.warn(formatClass(origin) + join(msgs));
  }

  @SafeVarargs
  public static <T, V> void warn(Class<T> origin, Throwable cause, V... msgs) {
    log.warn(formatClass(origin) + join(msgs), cause);
  }

  @SafeVarargs
  public static <T, V> void warn(boolean gameInfo, Class<T> origin, V... msgs) {
    log.warn(formatClass(origin) + join(msgs) + (gameInfo ? getGameInfo() : ""));
  }

  @SafeVarargs
  public static <T, V> void debug(Class<T> origin, V... msgs) {
    if (Cubepanion.get().configuration().getShowDebug().get()) {
      log.info(formatClass(origin) + join(msgs));
    }
  }

  @SafeVarargs
  public static <T, V> void debug(Class<T> origin, Throwable cause, V... msgs) {
    if (Cubepanion.get().configuration().getShowDebug().get()) {
      log.info(formatClass(origin) + join(msgs), cause);
    }
  }

  @SafeVarargs
  public static <T, V> void debug(boolean gameInfo, Class<T> origin, V... msgs) {
    if (Cubepanion.get().configuration().getShowDebug().get()) {
      log.info(formatClass(origin) + join(msgs) + (gameInfo ? getGameInfo() : ""));
    }
  }

  @SafeVarargs
  public static <T, V> void error(Class<T> origin, V... msgs) {
    log.error(formatClass(origin) + join(msgs));
  }

  @SafeVarargs
  public static <T, V> void error(Class<T> origin, Throwable cause, V... msgs) {
    log.error(formatClass(origin) + join(msgs), cause);
  }

  @SafeVarargs
  public static <T, V> void error(boolean gameInfo, Class<T> origin, V... msgs) {
    log.error(formatClass(origin) + join(msgs) + (gameInfo ? getGameInfo() : ""));
  }

}
