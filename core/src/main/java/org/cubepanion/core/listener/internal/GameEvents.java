package org.cubepanion.core.listener.internal;

import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.events.GameUpdateEvent;
import org.cubepanion.core.events.PlayerRespawnEvent;
import org.cubepanion.core.managers.CubepanionManager;

public class GameEvents {

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    String msg = e.chatMessage().getPlainText();

    if (msg.equals("Let the games begin!")) {
      CubepanionManager m = Cubepanion.get().getManager();
      m.setInPreLobby(false);
      m.setGameStartTime(System.currentTimeMillis());

      GameUpdateEvent event = new GameUpdateEvent(m.getDivision(), m.getDivision(), false);
      Laby.fireEvent(event);
      return;
    }

    if (msg.equals("You are now invincible for 10 seconds.")) {
      Laby.fireEvent(new PlayerRespawnEvent(true, SessionTracker.get().uuid()));
    }
  }

}
