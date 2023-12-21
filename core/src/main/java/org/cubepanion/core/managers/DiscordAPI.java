package org.cubepanion.core.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.network.ClientPacketListener;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.event.Subscribe;
import net.labymod.api.thirdparty.discord.DiscordActivity;
import net.labymod.api.thirdparty.discord.DiscordActivity.Asset;
import net.labymod.api.thirdparty.discord.DiscordActivity.Builder;
import net.labymod.api.util.I18n;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.DiscordRichPresenceSubConfig;
import org.cubepanion.core.events.GameUpdateEvent;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.utils.I18nNamespaces;

public class DiscordAPI {

  private static DiscordAPI instance;

  public static DiscordAPI getInstance() {
    return instance;
  }

  private final Cubepanion addon;
  private final String mainKey = I18nNamespaces.managerNameSpace + "DiscordRPCManager.";

  private boolean busy;

  private int deaths = 0;
  private int totalPlayers = 0;

  private final Set<String> removedPlayers = new HashSet<>();

  private final Set<UUID> removedPlayersUUID = new HashSet<>();

  public static void Init(Cubepanion addon) {
    instance = new DiscordAPI(addon);
  }

  public DiscordAPI(Cubepanion addon) {
    this.addon = addon;
  }

  public void removeCustomRPC() {
    this.addon.labyAPI().thirdPartyService().discord().displayDefaultActivity(false);
  }

  public void updateRPC() {
    if (this.busy) {
      return;
    }

    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }

    DiscordRichPresenceSubConfig RPCConfig = this.addon.configuration()
        .getDiscordRichPresenceSubConfig();
    if (!RPCConfig.isEnabled()) {
      return;
    }

    this.busy = true;

    DiscordActivity current = this.addon.labyAPI().thirdPartyService().discord()
        .getDisplayedActivity();

    Builder builder = DiscordActivity.builder(this);
    CubepanionManager manager = this.addon.getManager();

    if (current != null) {
      builder.start(current.getStartTime());
    }

    String details;
    String state;

    CubeGame division = manager.getDivision();

    if (division.equals(CubeGame.LOBBY)) {
      details = I18n.translate(this.mainKey + "lobby");
      state = I18n.translate(this.mainKey + "lobbyState");
    } else {
      details = I18n.translate(this.mainKey + "playing") + division.getString();
      if (manager.isInPreLobby() && !division.equals(CubeGame.FFA) && !division.equals(
          CubeGame.SKYBLOCK)) {
        state = I18n.translate(this.mainKey + "waitingState");
      } else {
        if (RPCConfig.map().get() && !manager.getMapName().isEmpty()) {
          state = I18n.translate(this.mainKey + "playingOnState", manager.getMapName());
        } else {
          state = I18n.translate(this.mainKey + "playingHiddenState");
        }

        if (RPCConfig.players().get() && this.doPlayerTracking()) {
          state += I18n.translate(this.mainKey + "alivePlayersState",
              this.totalPlayers - this.deaths, this.totalPlayers);
        }
      }
    }

    builder.details(details);
    builder.state(state);

    if (RPCConfig.getGameImage().get()) {
      builder.largeAsset(this.getGameAsset(division));
    } else {
      builder.largeAsset(Asset.of(
          "https://forums.cubecraftcdn.com/xenforo/data/avatars/o/307/307406.jpg?1591095808",
          "CubeCraft"));
    }

    this.addon.labyAPI().thirdPartyService().discord().displayActivity(builder.build());
    this.busy = false;
  }

  @Subscribe
  public void startOfGame(GameUpdateEvent e) {
    if (e.getDestination().equals(e.getOrigin()) && !e.isPreLobby()) {
      this.deaths = 0;
      this.totalPlayers = this.getTotalPlayers();
      this.removedPlayers.clear();
      this.removedPlayersUUID.clear();
      this.updateRPC();
    }
  }

  public void registerDeath(String name) {
    this.deaths++;
    this.removedPlayers.add(name);
    this.updateRPC();
  }

  public void registerDeath(NetworkPlayerInfo playerInfo) {
    if (checkIfAlreadyRemoved(playerInfo.profile().getProfileId())) {
      return;
    }
    for (Component c : playerInfo.displayName().getChildren()) {
      if (checkIfAlreadyRemoved(((TextComponent) c).getText())) {
        return;
      }
    }
    this.removedPlayersUUID.add(playerInfo.profile().getProfileId());
    this.deaths++;
    this.updateRPC();
  }

  private boolean checkIfAlreadyRemoved(UUID uuid) {
    for (UUID uuid1 : this.removedPlayersUUID) {
      if (uuid1.equals(uuid)) {
        return true;
      }
    }
    return false;
  }

  private boolean checkIfAlreadyRemoved(String name) {
    for (String n : this.removedPlayers) {
      if (n.equals(name)) {
        return true;
      }
    }
    return false;
  }


  private int getTotalPlayers() {
    ClientPacketListener clientPacketListener = this.addon.labyAPI().minecraft()
        .getClientPacketListener();
    return clientPacketListener == null ? 0 : clientPacketListener.getPlayerCount();
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

  private Asset getGameAsset(CubeGame game) {
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
