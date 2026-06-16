package art.ameliah.laby.addons.cubepanion.core.listener.misc;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.QOLConfig;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.ChestFinderLink;
import java.util.concurrent.TimeUnit;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import net.labymod.api.util.concurrent.task.Task;
import org.jetbrains.annotations.NotNull;

public class ChestFinder {

  private final Task task;
  private final QOLConfig config;
  private final ChestFinderLink finderLink;

  public ChestFinder(Cubepanion addon, @NotNull ChestFinderLink link) {
    config = addon.configuration().getQolConfig();
    finderLink = link;
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

    final var message = e.chatMessage().getPlainText();
    final String foundChestPart = "found the Hidden Chest!";
    final String chestMessage = "A chest has been hidden somewhere in the Lobby with some goodies inside!";

    if (message.equalsIgnoreCase(chestMessage)) {
      task.execute();
    } else if (message.contains(foundChestPart) && !message.contains(":")) {
      finderLink.clearLocations();
    }

  }

  @Subscribe
  public void onWorldChange(SubServerSwitchEvent e) {
    finderLink.clearLocations();
  }

}
