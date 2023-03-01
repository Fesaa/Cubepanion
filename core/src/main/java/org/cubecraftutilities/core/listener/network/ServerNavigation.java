package org.cubecraftutilities.core.listener.network;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.scoreboard.DisplaySlot;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.scoreboard.ScoreboardObjective;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
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
  public void onServerJoinEvent(ServerJoinEvent e) {
    String serverAddress = e.serverData().address().toString();
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
  public void onServerDisconnectEvent(ServerDisconnectEvent e) {
    if (this.manager.onCubeCraft()) {
      this.manager.reset();
      this.addon.rpcManager.removeCustomRPC();
      register_game_leave();
    }
  }

  private void register_game_leave() {
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
    if (statsTrackerSubConfig.isEnabled() && !this.manager.isWon() && !this.manager.isInPreLobby()) {
      GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(this.manager.getDivisionName());
      if (gameStatsTracker != null) {
        gameStatsTracker.registerLoss();
        gameStatsTracker.registerDeath("leave");
      }
    }
  }

  // This event is called when switching from server instance
  @Subscribe
  public void onSubServerSwitchEvent(SubServerSwitchEvent e) {
    if (!this.manager.onCubeCraft()) {
      return;
    }

    register_game_leave();
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
    ScoreboardObjective scoreboardObjective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
    if (scoreboardObjective == null) {
      Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()).schedule(() ->
              waitForScoreboard(scoreboard, maxDelay-100)
          , 100, TimeUnit.MILLISECONDS);
    } else {
      this.onServerSwitchAfterScoreboardLoad(scoreboard, Objects.requireNonNull(scoreboardObjective));
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
