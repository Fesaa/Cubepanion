package org.cubepanion.core.listener.misc;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.util.concurrent.task.Task;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.versionlinkers.ChestFinderLink;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;

public class ChestFinder {
    private final Task task;

  public ChestFinder(Cubepanion addon, @NotNull ChestFinderLink link) {
      task = Task.builder(() -> {
      if (addon.getManager().getDivision().equals(CubeGame.LOBBY)) {
        link.displayLocations();
      }
    }).delay(2000, TimeUnit.MILLISECONDS).build();
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
      String chestMessage = "A chest has been hidden somewhere in the Lobby with some goodies inside!";
      if (e.chatMessage().getPlainText().equals(chestMessage)) {
      task.execute();
    }
  }

}
