package art.ameliah.laby.addons.cubepanion.core.listener.misc;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.DiscordRichPresenceSubConfig;
import art.ameliah.laby.addons.cubepanion.core.events.GameStartEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerEliminationEvent;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import java.util.HashSet;
import java.util.Set;
import net.labymod.api.client.network.ClientPacketListener;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoRemoveEvent;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.thirdparty.discord.DiscordActivity;
import net.labymod.api.thirdparty.discord.DiscordActivity.Asset;
import net.labymod.api.thirdparty.discord.DiscordActivity.Builder;
import net.labymod.api.util.I18n;
import org.jetbrains.annotations.Nullable;

public class DiscordRPC {

  private final Cubepanion addon;
  private final Set<String> removedPlayers = new HashSet<>();
  private boolean busy;

  private int deaths = 0;
  private int totalPlayers = 0;

  public DiscordRPC(Cubepanion addon) {
    this.addon = addon;
  }

  private int getTotalPlayers() {
    ClientPacketListener clientPacketListener = this.addon.labyAPI().minecraft()
        .getClientPacketListener();
    return clientPacketListener == null ? 0 : clientPacketListener.getPlayerCount();
  }

  public void updateRPC() {
    if (busy) {
      return;
    }

    if (!addon.getManager().onCubeCraft()) {
      return;
    }

    DiscordRichPresenceSubConfig config = addon.configuration().getDiscordRichPresenceSubConfig();
    if (!config.isEnabled()) {
      this.addon.labyAPI().thirdPartyService().discord().displayDefaultActivity(true);
      return;
    }

    busy = true;
    DiscordActivity cur = addon.labyAPI().thirdPartyService().discord().getDisplayedActivity();
    CubepanionManager m = addon.getManager();
    Builder b = DiscordActivity.builder(this);

    if (cur != null) {
      b.start(cur.getStartTime());
    }

    String details;
    String state;
    CubeGame division = m.getDivision();

    if (division == CubeGame.LOBBY) {
      details = I18n.translate("cubepanion.managers.DiscordRPCManager.lobby");
      state = I18n.translate("cubepanion.managers.DiscordRPCManager.lobbyState");
    } else {
      details = I18n.translate("cubepanion.managers.DiscordRPCManager.playing")
          + division.getString();

      if (m.isInPreLobby()) {
        state = I18n.translate("cubepanion.managers.DiscordRPCManager.waitingState");
      } else {

        if (config.map().get() && !m.getMapName().isEmpty()) {
          state = I18n.translate("cubepanion.managers.DiscordRPCManager.playingOnState",
              m.getMapName());
        } else {
          state = I18n.translate("cubepanion.managers.DiscordRPCManager.playingHiddenState");
        }

        if (config.players().get() && doPlayerTracking()) {
          state += I18n.translate("cubepanion.managers.DiscordRPCManager.alivePlayersState",
              totalPlayers - deaths, totalPlayers);
        }
      }
    }

    b.details(details);
    b.state(state);

    if (config.getGameImage().get()) {
      b.largeAsset(getGameAsset(division));
    } else {
      b.largeAsset(getGameAsset(null));
    }

    addon.labyAPI().thirdPartyService().discord().displayActivity(b.build());
    busy = false;
  }

  @Subscribe
  public void onRequest(RequestEvent e) {
    if (e.getType() != RequestEvent.RequestType.UPDATE_RPC) {
      return;
    }

    updateRPC();
  }

  @Subscribe
  public void onServerDisconnectEvent(ServerDisconnectEvent e) {
    if (this.addon.getManager().onCubeCraft()) {
      this.addon.labyAPI().thirdPartyService().discord().displayDefaultActivity(true);
    }
  }

  @Subscribe
  public void startOfGame(GameStartEvent e) {
    deaths = 0;
    totalPlayers = this.getTotalPlayers();
    removedPlayers.clear();
    updateRPC();
  }

  @Subscribe
  public void onPlayerElimination(PlayerEliminationEvent e) {
    deaths++;
    removedPlayers.add(e.getName());
    updateRPC();
  }

  @Subscribe
  public void onPlayerRemove(PlayerInfoRemoveEvent e) {
    String name = e.playerInfo().profile().getUsername();
    if (removedPlayers.contains(name)) {
      return;
    }
    removedPlayers.add(name);
    deaths++;
    updateRPC();
  }

  private boolean doPlayerTracking() {
    switch (this.addon.getManager().getDivision()) {
      case SKYBLOCK, SIMPLE_PARKOUR, EASY_PARKOUR, MEDIUM_PARKOUR, HARD_PARKOUR, FFA -> {
        return false;
      }
      default -> {
        return true;
      }
    }
  }

  private Asset getGameAsset(@Nullable CubeGame game) {
    if (game == null) {
      return Asset.of(
          "https://forums.cubecraftcdn.com/xenforo/data/avatars/o/307/307406.jpg?1591095808",
          "CubeCraft");
    }
    switch (game) {
      case SKYBLOCK -> {
        return Asset.of(
            "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/skyblock.png",
            game.getString());
      }
      case TEAM_EGGWARS -> {
        return Asset.of(
            "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/eggwars.png",
            game.getString());
      }
      case SOLO_SKYWARS -> {
        return Asset.of(
            "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/skywars.png",
            game.getString());
      }
      case SOLO_LUCKYISLANDS -> {
        return Asset.of(
            "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/lucky-islands.png",
            game.getString());
      }
      case FFA -> {
        return Asset.of(
            "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/pvp.png",
            game.getString());
      }
      case SIMPLE_PARKOUR, EASY_PARKOUR, MEDIUM_PARKOUR, HARD_PARKOUR -> {
        return Asset.of(
            "https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/parkour.png",
            game.getString());
      }
      default -> {
        return Asset.of(
            "https://forums.cubecraftcdn.com/xenforo/data/avatars/o/307/307406.jpg?1591095808",
            "CubeCraft");
      }
    }
  }

}
