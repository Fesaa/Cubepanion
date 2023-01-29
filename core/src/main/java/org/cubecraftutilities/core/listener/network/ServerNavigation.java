package org.cubecraftutilities.core.listener.network;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.scoreboard.DisplaySlot;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.scoreboard.ScoreboardObjective;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.NetworkDisconnectEvent;
import net.labymod.api.event.client.network.server.NetworkLoginEvent;
import net.labymod.api.event.client.network.server.NetworkServerSwitchEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.CCUManager;
import org.cubecraftutilities.core.config.imp.GameStatsTracker;
import org.cubecraftutilities.core.config.subconfig.StatsTrackerSubConfig;
import org.cubecraftutilities.core.utils.AutoVote;
import org.jetbrains.annotations.NotNull;

public class ServerNavigation {

  private final CCU addon;
  private final CCUManager manager;

  public ServerNavigation(CCU addon) {
    this.addon = addon;
    this.manager = addon.getManager();
  }

  @Subscribe
  public void onNetworkLoginEvent(NetworkLoginEvent e) {
    String serverAddress = this.addon.labyAPI().serverController().getCurrentServerData().address().getAddress().toString();
    if (!(serverAddress.contains("cubecraft") || serverAddress.contains("ccgn.co"))) {
      this.manager.reset();
      return;
    }

    this.manager.onCubeJoin();
    this.executeWhereAmI();
    Minecraft minecraft = this.addon.labyAPI().minecraft();
    Scoreboard scoreboard = minecraft.getScoreboard();
    if (scoreboard == null) {
      return;
    }
    this.waitForScoreboard(scoreboard, 5000);
  }

  @Subscribe
  public void onNetworkDisconnectEvent(NetworkDisconnectEvent e) {
    if (this.manager.onCubeCraft()) {
      this.manager.reset();
      this.addon.rpcManager.removeCustomRPC();
    }
  }

  // This event is called when switching from server instance
  @Subscribe
  public void onNetworkServerSwitchEvent(NetworkServerSwitchEvent e) {
    if (!this.manager.onCubeCraft()) {
      return;
    }

    // Win Streak Counter
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
    if (statsTrackerSubConfig.isEnabled() && !this.manager.isWon() && !this.manager.isInPreLobby()) {
      GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(this.manager.getDivisionName());
      if (gameStatsTracker != null) {
        gameStatsTracker.registerLoss();
      }
    }

    this.executeWhereAmI();
    this.manager.onServerSwitch();

    Minecraft minecraft = this.addon.labyAPI().minecraft();
    Scoreboard scoreboard = minecraft.getScoreboard();
    if (scoreboard == null) {
      return;
    }
    this.waitForScoreboard(scoreboard, 2000);
  }

  private void executeWhereAmI() {
    if (this.addon.configuration().displayWhereAmI().get()) {
      this.addon.labyAPI().minecraft().chatExecutor().chat("/whereami", false);
    }
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
      this.onServerSwitchAfterScoreboardLoad(scoreboard, Objects.requireNonNull(scoreboard.objective(DisplaySlot.SIDEBAR)));
    }
  }

  public void onServerSwitchAfterScoreboardLoad(@NotNull Scoreboard scoreboard, @NotNull ScoreboardObjective scoreboardObjective) {
    this.manager.registerNewDivision(scoreboard, scoreboardObjective);

    if (this.addon.configuration().getAutoVoteSubConfig().isEnabled()) {
      AutoVote.vote(this.addon);
    }
    this.addon.rpcManager.updateRPC();
  }
}
