package org.ccu.core.listener;

import com.google.inject.Inject;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.player.GameMode;
import net.labymod.api.client.scoreboard.DisplaySlot;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.scoreboard.ScoreboardObjective;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoRemoveEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent;
import net.labymod.api.event.client.network.playerinfo.PlayerInfoUpdateEvent.UpdateType;
import net.labymod.api.event.client.network.server.NetworkDisconnectEvent;
import net.labymod.api.event.client.network.server.NetworkLoginEvent;
import net.labymod.api.event.client.network.server.NetworkServerSwitchEvent;
import org.ccu.core.CCU;
import org.ccu.core.config.CCUManager;
import org.ccu.core.config.imp.GameStatsTracker;
import org.ccu.core.config.subconfig.StatsTrackerSubConfig;
import org.ccu.core.utils.AutoVote;
import org.jetbrains.annotations.NotNull;

public class NetworkListener {

  private final CCU addon;
  private final CCUManager manager;

  @Inject
  public NetworkListener(CCU addon) {
    this.addon = addon;
    this.manager = this.addon.getManager();
  }

  @Subscribe
  public void onPlayerInfoRemoveEvent(PlayerInfoRemoveEvent playerInfoRemoveEvent) {
    if (!this.manager.isInPreLobby() && this.manager.getDivisionName().equals("Team EggWars")) {
      this.addon.rpcManager.registerDeath(playerInfoRemoveEvent.playerInfo());
    }
  }

  @Subscribe
  public void onPlayerInfoUpdateEvent(PlayerInfoUpdateEvent playerInfoUpdateEvent) {
    if (this.manager.getDivisionName().equals("Team EggWars")) {

      if (playerInfoUpdateEvent.type().equals(UpdateType.GAME_MODE) && playerInfoUpdateEvent.playerInfo().gameMode().equals(GameMode.SURVIVAL)) {
        this.addon.getManager().getSpawnProtectionManager().registerDeath(playerInfoUpdateEvent.playerInfo().profile().getUniqueId());
      }
      return;
    }
    if (playerInfoUpdateEvent.type().equals(UpdateType.GAME_MODE)) {
      if (playerInfoUpdateEvent.playerInfo().gameMode().equals(GameMode.SPECTATOR)) {
        this.addon.rpcManager.registerDeath(playerInfoUpdateEvent.playerInfo());
      }
    }
  }

  @Subscribe
  public void onNetworkLoginEvent(NetworkLoginEvent networkLoginEvent) {
    String serverAddress = this.addon.labyAPI().serverController().getCurrentServerData().address().getAddress().toString();
    if (!(serverAddress.contains("cubecraft") || serverAddress.contains("ccgn.co"))) {
      this.manager.reset();
      return;
    }

    this.manager.onCubeJoin();
    cubeProcesses();
  }

  @Subscribe
  public void onNetworkDisconnectEvent(NetworkDisconnectEvent networkDisconnectEvent) {
    this.manager.reset();
    this.addon.rpcManager.removeCustomRPC();
  }

  @Subscribe
  public void onNetworkServerSwitchEvent(NetworkServerSwitchEvent networkServerSwitchEvent) {

    // Win Streak Counter
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
    if (statsTrackerSubConfig.isEnabled() && !this.manager.isWon() && !this.manager.isInPreLobby()) {
      GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(this.manager.getDivisionName());
      if (gameStatsTracker != null) {
        gameStatsTracker.registerLoss();
      }
    }

    if (this.manager.onCubeCraft()) {
      cubeProcesses();
    }
  }

  //TODO: Rewrite cubeProcesses, it's message and not clear what it's supposed to do
  // Give each sub function its own function to call from the listeners for more flexibility
  private void cubeProcesses() {
    if (this.addon.configuration().displayWhereAmI().get()) {
      this.addon.labyAPI().minecraft().chatExecutor().chat("/whereami", false);
    }

    this.manager.setEliminated(false);
    this.manager.setInPreLobby(true);
    this.manager.setChangedColour(false);

    Minecraft minecraft = this.addon.labyAPI().minecraft();
    Scoreboard scoreboard = minecraft.getScoreboard();
    if (scoreboard == null) {
      return;
    }

    waitForScoreboard(scoreboard, 5000);
  }

  private void waitForScoreboard(@NotNull Scoreboard scoreboard, int maxDelay) {
    if (maxDelay == 0) {
      return;
    }
    if (scoreboard.objective(DisplaySlot.SIDEBAR) == null) {
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).schedule(() ->
              waitForScoreboard(scoreboard, maxDelay-100)
          , 100, TimeUnit.MILLISECONDS);
    } else {
      toRun(scoreboard, Objects.requireNonNull(scoreboard.objective(DisplaySlot.SIDEBAR)));
    }
  }

  private void toRun(@NotNull Scoreboard scoreboard, @NotNull ScoreboardObjective scoreboardObjective) {
    this.manager.registerNewDivision(scoreboard, scoreboardObjective);

    if (this.addon.configuration().getAutoVoteSubConfig().isEnabled()) {
      AutoVote.vote(this.addon);
    }
    this.addon.rpcManager.updateRPC();
  }

}
