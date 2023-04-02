package org.cubecraftutilities.core.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.network.ClientPacketListener;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.thirdparty.discord.DiscordActivity;
import net.labymod.api.thirdparty.discord.DiscordActivity.Asset;
import net.labymod.api.thirdparty.discord.DiscordActivity.Builder;
import net.labymod.api.util.I18n;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.subconfig.DiscordRichPresenceSubConfig;
import org.cubecraftutilities.core.utils.I18nNamespaces;

public class DiscordRPCManager {

  private final CCU addon;
  private final String mainKey = I18nNamespaces.managerNameSpace + "DiscordRPCManager.";

  private boolean busy;

  private int deaths = 0;
  private int totalPlayers = 0;

  private List<String> removedPlayers = new ArrayList<>();

  private List<UUID> removedPlayersUUID = new ArrayList<>();

  public DiscordRPCManager(CCU addon) {
    this.addon = addon;
  }

  public void removeCustomRPC() {
    this.addon.labyAPI().thirdPartyService().discord().displayDefaultActivity();
  }

  public void updateRPC() {
    if (this.busy) {
      return;
    }

    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }

    DiscordRichPresenceSubConfig RPCConfig = this.addon.configuration().getDiscordRichPresenceSubConfig();
    if (!RPCConfig.isEnabled()) {
      this.addon.labyAPI().thirdPartyService().discord().displayDefaultActivity();
      return;
    }

    this.busy = true;

    DiscordActivity current = this.addon.labyAPI().thirdPartyService().discord().getDisplayedActivity();

    Builder builder = DiscordActivity.builder(this);
    CCUManager manager = this.addon.getManager();

    if (current != null) {
      builder.start(current.getStartTime());
    }

    String details;
    String state;

    String division = manager.getDivisionName();

    if (division.equals("CubeCraft") || division.equals("sidebar")) {
      details = I18n.translate(this.mainKey + "lobby");
      state = I18n.translate(this.mainKey + "lobbyState");
    } else {
      details = I18n.translate(this.mainKey + "playing") + division;
      if (manager.isInPreLobby() && !manager.getDivisionName().equals("FFA")) {
        state = I18n.translate(this.mainKey + "waitingState");
      } else {
        if (RPCConfig.map().get() && !manager.getMapName().equals("")) {
          state = I18n.translate(this.mainKey + "playingOnState", manager.getMapName());
        } else {
          state = I18n.translate(this.mainKey + "playingHiddenState");
        }

        if (RPCConfig.players().get() && this.doPlayerTracking()) {
          state += I18n.translate(this.mainKey + "alivePlayersState", this.totalPlayers - this.deaths, this.totalPlayers);
        }
      }
    }

    builder.details(details);
    builder.state(state);

    if (RPCConfig.getGameImage().get()) {
      builder.largeAsset(this.getGameAsset(division));
    } else {
      builder.largeAsset(Asset.of("https://forums.cubecraftcdn.com/xenforo/data/avatars/o/307/307406.jpg?1591095808", "CubeCraft"));
    }

    this.addon.labyAPI().thirdPartyService().discord().displayActivity(builder.build());
    this.busy = false;
  }

  public void startOfGame() {
    this.deaths = 0;
    this.totalPlayers = this.getTotalPlayers();
    this.removedPlayers = new ArrayList<>();
    this.removedPlayersUUID =  new ArrayList<>();

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
    ClientPacketListener clientPacketListener = this.addon.labyAPI().minecraft().getClientPacketListener();
    return clientPacketListener == null ?  0 : clientPacketListener.getPlayerCount();
  }

  private boolean doPlayerTracking() {
    switch (this.addon.getManager().getDivisionName()) {
      case "Skyblock":
      case "Simple Parkour":
      case "Normal Parkour":
      case "Medium Parkour":
      case "Hard Parkour":
      case "FFA": {
        return false;
      }
      default: {
        return true;
      }
    }
  }

  private Asset getGameAsset(String game) {
    switch (game) {
      case "Skyblock": {
        return Asset.of("https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/skyblock.png", game);
      }
      case "Team EggWars": {
        return Asset.of("https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/eggwars.png", game);
      }
      case "Solo SkyWars": {
        return Asset.of("https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/skywars.png", game);
      }
      case "Lucky Islands": {
        return Asset.of("https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/lucky-islands.png", game);
      }
      case "FFA": {
        return Asset.of("https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/pvp.png", game);
      }
      case "Simple Parkour":
      case "Normal Parkour":
      case "Medium Parkour":
      case "Hard Parkour": {
        return Asset.of("https://forums.cubecraftcdn.com/xenforo/serve/styles/cubecraft/cubecraft/minigames/node-icons/parkour.png", game);
      }
      default: {
        return Asset.of("https://forums.cubecraftcdn.com/xenforo/data/avatars/o/307/307406.jpg?1591095808", "CubeCraft");
      }
    }
  }
}
