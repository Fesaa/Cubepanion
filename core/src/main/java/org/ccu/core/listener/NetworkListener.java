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
import org.ccu.core.config.imp.GameStatsTracker;
import org.ccu.core.config.internal.CCUinternalConfig;
import org.ccu.core.config.subconfig.StatsTrackerSubConfig;
import org.jetbrains.annotations.NotNull;

public class NetworkListener {

  private final CCU addon;

  @Inject
  public NetworkListener(CCU addon) {this.addon = addon;}

  @Subscribe
  public void onPlayerInfoRemoveEvent(PlayerInfoRemoveEvent playerInfoRemoveEvent) {
    if (!CCUinternalConfig.inPreLobby
        && CCUinternalConfig.name.equals("Team EggWars")) {
      this.addon.rpcManager.registerDeath(playerInfoRemoveEvent.playerInfo());
      this.addon.logger().info("onPlayerInfoRemoveEvent + 1 death");

    }
  }

  @Subscribe
  public void onPlayerInfoUpdateEvent(PlayerInfoUpdateEvent playerInfoUpdateEvent) {
    if (CCUinternalConfig.name.equals("Team EggWars")) {
      return;
    }
    if (playerInfoUpdateEvent.type().equals(UpdateType.GAME_MODE)) {
      if (playerInfoUpdateEvent.playerInfo().gameMode().equals(GameMode.SPECTATOR)) {
        this.addon.rpcManager.registerDeath(playerInfoUpdateEvent.playerInfo());
        this.addon.logger().info("onPlayerInfoUpdateEvent + 1 death");
      }
    }
  }

  @Subscribe
  public void onNetworkLoginEvent(NetworkLoginEvent networkLoginEvent) {
    cubeProcesses();
  }

  @Subscribe
  public void onNetworkDisconnectEvent(NetworkDisconnectEvent networkDisconnectEvent) {
    CCUinternalConfig.resetVars();
    this.addon.rpcManager.removeCustomRPC();
  }

  @Subscribe
  public void onNetworkServerSwitchEvent(NetworkServerSwitchEvent networkServerSwitchEvent) {

    // Win Streak Counter
    StatsTrackerSubConfig statsTrackerSubConfig = this.addon.configuration().getStatsTrackerSubConfig();
    if (statsTrackerSubConfig.isEnabled() && !CCUinternalConfig.won && !CCUinternalConfig.inPreLobby) {
      GameStatsTracker gameStatsTracker = statsTrackerSubConfig.getGameStatsTrackers().get(CCUinternalConfig.name);
      if (gameStatsTracker != null) {
        gameStatsTracker.registerLoss();
      }
    }

    cubeProcesses();
  }

  private void cubeProcesses() {
    String serverAddress = this.addon.labyAPI().serverController().getCurrentServerData().address().getAddress().toString();

    if (!(serverAddress.contains("cubecraft") || serverAddress.contains("ccgn.co"))) {
      CCUinternalConfig.resetVars();
      return;
    }

    if (this.addon.configuration().displayWhereAmI().get()) {
      this.addon.labyAPI().minecraft().chatExecutor().chat("/whereami", false);
    }

    CCUinternalConfig.hasSaidGG = false;
    CCUinternalConfig.inPreLobby = true;

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
    CCUinternalConfig.setVars(this.addon, scoreboard, scoreboardObjective);

    if (this.addon.configuration().getAutoVoteSubConfig().isEnabled()) {
      //AutoVote.vote();
      this.addon.logger().info("Tried to AutoVote");
    }
    this.addon.rpcManager.updateRPC();
  }

}
