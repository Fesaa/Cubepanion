package org.cubepanion.core.listener.games;

import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.EndGameSubConfig;
import org.cubepanion.core.config.subconfig.EndGameSubConfig.GameEndMessage;
import org.cubepanion.core.managers.CubepanionManager;

public class AutoGG {

  private final EndGameSubConfig config;
  private final CubepanionManager manager;

  public AutoGG() {
    this.config = Cubepanion.get().configuration().getAutomationConfig().getEndGameSubConfig();
    this.manager = Cubepanion.get().getManager();
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    if (!config.isEnabled().get() || manager.isEliminated()) {
      return;
    }

    Minecraft minecraft = Laby.labyAPI().minecraft();
    String msg = e.chatMessage().getPlainText();
    ClientPlayer p = minecraft.getClientPlayer();
    if (p == null) {
      return;
    }

    // TODO make this a global variable that changes on authorize event or something
    String playerRegex = ".{0,5}" + p.getName() + ".{0,5}";

    String eliminationMessage = playerRegex + " has been eliminated from the game.";
    boolean doElim = msg.matches(eliminationMessage) && config.getOnElimination().get();

    if (msg.equals("Congratulations, you win!") || doElim) {
      GameEndMessage gameEndMessage = config.getGameEndMessage().get();
      gameEndMessage.send(minecraft.chatExecutor(), config,
          manager.getPartyManager().isInParty());
      manager.setEliminated(true);
    }
  }

}
