package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.EndGameSubConfig;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.EndGameSubConfig.GameEndMessage;
import art.ameliah.laby.addons.cubepanion.core.events.GameEndEvent;
import art.ameliah.laby.addons.cubepanion.core.events.GameJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerDeathEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerEliminationEvent;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;

public class AutoGG {

  private final EndGameSubConfig config;
  private final CubepanionManager manager;
  private boolean hasSentGG = false;

  public AutoGG() {
    this.config = Cubepanion.get().configuration().getAutomationConfig().getEndGameSubConfig();
    this.manager = Cubepanion.get().getManager();
  }

  @Subscribe
  public void onGameUpdate(GameJoinEvent e) {
    hasSentGG = false;
  }

  @Subscribe
  public void onPlayerElimination(PlayerEliminationEvent e) {
    if (!config.isEnabled().get()) {
      return;
    }
    if (!config.getOnElimination().get()) {
      return;
    }
    if (hasSentGG) {
      return;
    }
    if (!e.isClientPlayer()) {
      return;
    }
    
    if (manager.isPlaying(CubeGame.SNOWMAN_SURVIVAL)) {
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
    if (!config.getOnElimination().get()) {
      return;
    }
    if (hasSentGG || !e.isClientPlayer()) {
      return;
    }
    if (manager.isPlaying(CubeGame.TEAM_EGGWARS)
        || manager.isPlaying(CubeGame.FFA)
        || manager.isPlaying(CubeGame.SKYBLOCK)
        || manager.isPlaying(CubeGame.BEDWARS)
        || manager.isPlaying(CubeGame.SNOWMAN_SURVIVAL)
        || CubeGame.isParkour(manager.getDivision())) {
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
