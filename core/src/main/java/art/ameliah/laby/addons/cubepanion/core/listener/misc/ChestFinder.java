package art.ameliah.laby.addons.cubepanion.core.listener.misc;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.QOLConfig;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.ChestFinderLink;
import java.util.concurrent.TimeUnit;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.concurrent.task.Task;
import org.jetbrains.annotations.NotNull;

public class ChestFinder {

  private final Task task;
  private final QOLConfig config;

  public ChestFinder(Cubepanion addon, @NotNull ChestFinderLink link) {
    config = addon.configuration().getQolConfig();

    task = Task.builder(() -> {
      if (addon.getManager().getDivision().equals(CubeGame.LOBBY)) {
        link.displayLocations();
      }
    }).delay(2000, TimeUnit.MILLISECONDS).build();
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    if (!config.getChestLocation().get()) {
      return;
    }
    String chestMessage = "A chest has been hidden somewhere in the Lobby with some goodies inside!";
    if (e.chatMessage().getPlainText().equals(chestMessage)) {
      task.execute();
    }
  }

}
