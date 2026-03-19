package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.AutoPlaySubConfig;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.serializer.plain.PlainTextComponentSerializer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.concurrent.task.Task;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class AutoPlay {

  private static final String playAgainCommand =
      "/playagain now";

  private static final PlainTextComponentSerializer serializer =
      PlainTextComponentSerializer.plainText();

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

    if (!CubeGame.isMiniGame(this.manager.getDivision())) {
      return;
    }

    final Runnable runnable = () -> Laby.labyAPI()
        .minecraft()
        .chatExecutor()
        .chat(playAgainCommand, false);

    event.message()
        .getChildren()
        .stream()
        .flatMap(this::flattenComponent)
        .filter(
            (component) ->
                component.toBuilder().hasClickEvent() &&
                    component.style().getClickEvent()
                        .getValue().equalsIgnoreCase(playAgainCommand)
        )
        .findFirst()
        .ifPresent(
            (ignored) ->
                Task.builder(runnable)
                    .delay(this.config.getDelay().get(), TimeUnit.MILLISECONDS)
                    .build()
                    .execute()
        );

  }

  private Stream<Component> flattenComponent(Component component) {
    return Stream.concat(
        Stream.of(component),
        component.getChildren()
            .stream()
            .flatMap(this::flattenComponent)
    );
  }

}
