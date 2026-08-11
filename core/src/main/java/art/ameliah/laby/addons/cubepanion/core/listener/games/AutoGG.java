package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.EndGameSubConfig;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.EndGameSubConfig.GameEndMessage;
import art.ameliah.laby.addons.cubepanion.core.events.GameEndEvent;
import art.ameliah.laby.addons.cubepanion.core.events.GameJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerDeathEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerEliminationEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PrintDebugRequestEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PrintDebugRequestEvent.Receiver;
import art.ameliah.laby.addons.cubepanion.core.external.GameFlag;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
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
    
    if (manager.getGame().hasFlagEnabled(GameFlag.IN_GAME_AFTER_ELIMINATION)) {
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
    if (manager.getGame().hasFlagEnabled(GameFlag.HAS_RESPAWNS)) {
      return;
    }

    doMessage();
  }

  @Subscribe
  public void onPrintDebugRequest(PrintDebugRequestEvent e) {
    if (e.receiver() == Receiver.AutoGG) {
      Laby.labyAPI().minecraft().chatExecutor().chat(this.debug(), false);
    }
  }

  private void doMessage() {
    GameEndMessage gameEndMessage = config.getGameEndMessage().get();
    gameEndMessage.send(Laby.labyAPI().minecraft().chatExecutor(), config,
        manager.getPartyManager().isInParty());
    hasSentGG = true;
  }

  private String debug() {
    return String.format(
        "AutoGG{" +
            "enabled=%s, " +
            "onElimination=%s, " +
            "hasSentGG=%s, " +
            "division=%s, " +
            "inParty=%s, " +
            "message=%s, " +
            "hasRespawns=%s" +
            "}",
        config.isEnabled().get(),
        config.getOnElimination().get(),
        hasSentGG,
        manager.getGame(),
        manager.getPartyManager().isInParty(),
        config.getGameEndMessage().get(),
        manager.getGame().hasFlagEnabled(GameFlag.HAS_RESPAWNS)
    );
  }
}
