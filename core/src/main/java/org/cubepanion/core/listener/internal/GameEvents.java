package org.cubepanion.core.listener.internal;

import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.events.PlayerRespawnEvent;

public class GameEvents {

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    String msg = e.chatMessage().getPlainText();

    ClientPlayer player = Laby.labyAPI().minecraft().getClientPlayer();
    if (player == null) {
      return;
    }

    if (msg.equals("Let the games begin!")) {
      Cubepanion.get().getManager().setInPreLobby(false);
      Cubepanion.get().getManager().setGameStartTime(System.currentTimeMillis());
      return;
    }

    if (msg.equals("You are now invincible for 10 seconds.")) {
      Laby.fireEvent(new PlayerRespawnEvent(true, player.getUniqueId()));
    }
  }

}
