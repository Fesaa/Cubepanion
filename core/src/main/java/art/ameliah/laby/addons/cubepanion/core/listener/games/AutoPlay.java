package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.AutoPlaySubConfig;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.concurrent.task.Task;
import java.util.concurrent.TimeUnit;

public class AutoPlay {

  private static final String playAgainCommand = "/playagain now";

  private final AutoPlaySubConfig config;
  private final CubepanionManager manager;

  public AutoPlay(Cubepanion cubepanion) {
    this.manager = cubepanion.getManager();
    this.config = cubepanion.configuration()
        .getAutomationConfig()
        .getAutoPlaySubConfig();
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent event) {

    if (!this.config.isEnabled()) {
      return;
    }

    if (!this.manager.onCubeCraft()) {
      return;
    }

    if (!CubeGame.isMiniGame(this.manager.getDivision())) {
      return;
    }

    final var message = event.chatMessage().getPlainText();
    if (message.contains(":")) {
      return;
    }

    if (!message.contains("Play Again") && !message.contains("Leave")) {
      return;
    }

    final Runnable runnable = () -> Laby.labyAPI()
        .minecraft()
        .chatExecutor()
        .chat(playAgainCommand, false);

    Task.builder(runnable)
        .delay(this.config.getDelay().get(), TimeUnit.MILLISECONDS)
        .build()
        .execute();

  }

}
