package art.ameliah.laby.addons.cubepanion.core.cubesocket.session;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets.PacketGameStatUpdate;
import art.ameliah.laby.addons.cubepanion.core.events.GameUpdateEvent;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamEntryAddEvent;
import net.labymod.api.util.concurrent.task.Task;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CubeSocketPlayerCountTracker {

  private final Cubepanion addon;
  private final CubeSocket socket;

  private final Deque<PacketGameStatUpdate> packetGameStatUpdates = new ArrayDeque<>();

  private Task sendTask;

  private boolean maySendLobby = true;
  private boolean maySendGame = true;
  private final HashSet<CubeGame> hasSendGame = new HashSet<>();

  public CubeSocketPlayerCountTracker(CubeSocket socket, Cubepanion addon) {
    this.socket = socket;
    this.addon = addon;

    // Don't spam the socket, doesn't really like it. Might look into this later, but not worth it for now
    // I'm not even sure if it's not just my internet here. But it's fine to do it this way.
    this.sendTask = Task.builder(() -> {
      if (this.packetGameStatUpdates.isEmpty()) {
        return;
      }

      if (!this.addon.getManager().isDevServer()) {
        this.socket.sendPacket(this.packetGameStatUpdates.poll());
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

    String playerCountString = ((TextComponent) playerCount.getLast()).getText().replace(",", "");
    try {
      int playerCountInt = Integer.parseInt(playerCountString);
      this.maySendLobby = false;
      if (!this.addon.getManager().isDevServer()) {
        this.socket.sendPacket(new PacketGameStatUpdate(CubeGame.LOBBY, playerCountInt));
      }
    } catch (NumberFormatException e) {
      LOGGER.error(getClass(), "Failed to parse playercount from scoreboard: " + playerCountString);
    }
  }

  @Subscribe
  public void onLobbyJoin(GameUpdateEvent event) {
    // Only update stuff on new lobby joins, don't need to spam insignificant updates
    if (event.getDestination().equals(CubeGame.LOBBY) && !event.getOrigin().equals(CubeGame.LOBBY)) {
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

    functionLink.loadPlayerCounts().thenApplyAsync(res -> {
      if (res == null) {
        return null;
      }

      res.forEach((game, playerCount) -> {
        if (playerCount == null || this.hasSendGame.contains(game)) {
          return;
        }
        this.hasSendGame.add(game);
        this.packetGameStatUpdates.add(new PacketGameStatUpdate(game, playerCount));
      });

      this.sendTask.execute();
      this.maySendGame = false;
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

}
