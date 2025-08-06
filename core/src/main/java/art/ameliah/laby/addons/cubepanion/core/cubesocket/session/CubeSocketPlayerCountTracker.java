package art.ameliah.laby.addons.cubepanion.core.cubesocket.session;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketGameStatUpdate;
import art.ameliah.laby.addons.cubepanion.core.events.GameJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.external.Game;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamEntryAddEvent;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.logging.Logging;

public class CubeSocketPlayerCountTracker {

  private final Logging log = Logging.create(CubeSocket.class);

  private final Cubepanion addon;
  private final CubeSocket socket;

  private final Deque<PacketGameStatUpdate> packetGameStatUpdates = new ArrayDeque<>();

  private Task sendTask;

  private boolean maySendLobby = true;
  private boolean maySendGame = true;
  private final HashSet<Game> hasSendGame = new HashSet<>();
  private final Map<Game, Long> lastSend = new ConcurrentHashMap<>();

  public CubeSocketPlayerCountTracker(CubeSocket socket, Cubepanion addon) {
    this.socket = socket;
    this.addon = addon;

    // Don't spam the socket, doesn't really like it. Might look into this later, but not worth it for now
    // I'm not even sure if it's not just my internet here. But it's fine to do it this way.
    this.sendTask = Task.builder(() -> {
      if (this.packetGameStatUpdates.isEmpty()) {
        return;
      }
      PacketGameStatUpdate packet = this.packetGameStatUpdates.poll();
      if (!this.addon.getManager().isDevServer()) {
        this.lastSend.put(packet.getGame(), System.currentTimeMillis());
        this.socket.sendPacket(packet);
      }

      if (!this.packetGameStatUpdates.isEmpty()) {
        this.sendTask.execute();
      }
    }).delay(1, TimeUnit.SECONDS).build();
  }

  @Subscribe
  public void onScoreboardLineAdd(ScoreboardTeamEntryAddEvent event) {
    if (this.cantUpdate(this.maySendLobby)) {
      return;
    }

    List<Component> children = event.team().getPrefix().getChildren();
    if (children.isEmpty()) {
      return;
    }

    List<Component> playerCount = children.getFirst().getChildren();
    if (playerCount.size() != 2) {
      return;
    }

    if (!((TextComponent) playerCount.getFirst()).getText().contains("Players: ")) {
      return;
    }

    int playerCountInt;
    try {
      playerCountInt = Integer.parseInt(((TextComponent) playerCount.getLast()).getText().replace(",", ""));
    } catch (NumberFormatException e) {
      log.error("Failed to parse player count string for lobby {}", e);
      return;
    }

    this.maySendLobby = false;
    if (!this.addon.getManager().isDevServer()) {
      log.debug("Updating lobby player count to {}", playerCountInt);
      this.socket.sendPacket(new PacketGameStatUpdate(Game.LOBBY, playerCountInt));
    }
  }

  @Subscribe
  public void onLobbyJoin(GameJoinEvent event) {
    // Only update stuff on new lobby joins, don't need to spam insignificant updates
    if (event.getDestination().equals(CubeGame.LOBBY) && !event.getOrigin()
        .equals(CubeGame.LOBBY)) {
      // Don't update if we're still sending these
      if (this.packetGameStatUpdates.isEmpty()) {
        this.hasSendGame.clear();
        this.maySendGame = true;
      }
    }

    // The scoreboard event is fired before this one, so reset it when joining anything else
    if (!event.getDestination().equals(CubeGame.LOBBY)) {
      this.maySendLobby = true;
    }
  }

  @Subscribe
  public void readGamePlayerCounts(ScreenDisplayEvent e) {
    FunctionLink functionLink = this.addon.getFunctionLink();
    if (functionLink == null) {
      return;
    }
    if (this.cantUpdate(this.maySendGame)) {
      return;
    }

    functionLink.loadMenuItems(title -> title.contains("Games"))
        .thenApplyAsync(items -> {
          if (items == null || items.isEmpty()) {
            return null;
          }

          HashMap<Game, Integer> map = new HashMap<>();
          for (var item : items) {
            if (item.isAir()) {
              continue;
            }

            this.readPlayerCount(map, item);
          }
          return map;
        }).thenApplyAsync(res -> {
          if (res == null) {
            return null;
          }

          log.debug("found {} games with a player count", res.size());
          res.forEach((game, playerCount) -> {
            if (playerCount == null || this.hasSendGame.contains(game)) {
              return;
            }

            Long last = lastSend.get(game);
            if (last != null && (System.currentTimeMillis() - last < TimeUnit.MINUTES.toMillis(1))) {
              log.debug("ignoring update for {} as it has been less than 1m since last submission", game);
              return;
            }


            log.debug("updating {} to {}", game.displayName(), playerCount);
            this.hasSendGame.add(game);
            this.packetGameStatUpdates.add(new PacketGameStatUpdate(game, playerCount));
          });

          this.maySendGame = false;
          this.sendTask.execute();
          return null;
        });
  }

  private boolean cantUpdate(boolean maySend) {
    if (!this.addon.getManager().onCubeCraft() || !this.socket.isConnected()) {
      return true;
    }

    if (!this.addon.getManager().getDivision().equals(CubeGame.LOBBY)) {
      return true;
    }

    return !maySend;
  }

  private void readPlayerCount(HashMap<Game, Integer> games, CCItemStack item) {
    if (item.getDisplayName().getChildren().isEmpty() || item.getDisplayName().getChildren()
        .getFirst().getChildren().isEmpty()) {
      return;
    }

    String name = ((TextComponent) item.getDisplayName().getChildren().getFirst().getChildren()
        .getFirst()).getText();
    Game game = CubepanionAPI.I().tryGame(name);
    if (game == null) {
      return;
    }

    List<String> toolTips = item.getToolTips();
    if (toolTips.size() < 2) {
      return;
    }

    for (String content : toolTips) {
      if (content.contains("Players: ")) {
        String playerCountString = content.replace("Players: ", "");
        try {
          int playerCount = Integer.parseInt(playerCountString);
          games.put(game, playerCount);
          break;
        } catch (NumberFormatException e) {
          log.warn("Could not parse player count {} for name {}", playerCountString,
              game.displayName());
        }
      }
    }
  }

}
