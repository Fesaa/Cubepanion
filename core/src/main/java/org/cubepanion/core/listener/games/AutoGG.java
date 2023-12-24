package org.cubepanion.core.listener.games;

import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.EndGameSubConfig;
import org.cubepanion.core.config.subconfig.EndGameSubConfig.GameEndMessage;
import org.cubepanion.core.events.GameEndEvent;
import org.cubepanion.core.events.GameUpdateEvent;
import org.cubepanion.core.events.PlayerDeathEvent;
import org.cubepanion.core.events.PlayerEliminationEvent;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.utils.CubeGame;

public class AutoGG {

  private final EndGameSubConfig config;
  private final CubepanionManager manager;
  private boolean hasSentGG = false;

  public AutoGG() {
    this.config = Cubepanion.get().configuration().getAutomationConfig().getEndGameSubConfig();
    this.manager = Cubepanion.get().getManager();
  }

  @Subscribe
  public void onGameUpdate(GameUpdateEvent e) {
    hasSentGG = false;
  }

  @Subscribe
  public void onPlayerElimination(PlayerEliminationEvent e) {
    if (!config.isEnabled().get()) {
      return;
    }
    if (hasSentGG) {
      return;
    }
    if (!e.isClientPlayer()) {
      return;
    }
    doMessage();
  }

  @Subscribe
  public void onGameEnd(GameEndEvent e) {
    if (!config.isEnabled().get()) {
      return;
    }
    if (hasSentGG) {
      return;
    }
    if (e.hasSwitchedServer()) {
      return;
    }
    doMessage();
  }

  @Subscribe
  public void onDeath(PlayerDeathEvent e) {
    if (!config.isEnabled().get()) {
      return;
    }
    if (hasSentGG || !e.isClientPlayer()) {
      return;
    }
    if (Cubepanion.get().getManager().isPlaying(CubeGame.TEAM_EGGWARS)
        || Cubepanion.get().getManager().isPlaying(CubeGame.FFA)
        || Cubepanion.get().getManager().isPlaying(CubeGame.SKYBLOCK)
        || CubeGame.isParkour(Cubepanion.get().getManager().getDivision())) {
      return;
    }
    doMessage();
  }

  private void doMessage() {
    GameEndMessage gameEndMessage = config.getGameEndMessage().get();
    gameEndMessage.send(Laby.labyAPI().minecraft().chatExecutor(), config,
        manager.getPartyManager().isInParty());
    hasSentGG = true;
  }
}
