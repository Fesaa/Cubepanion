package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.GameStartEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerRespawnEvent;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

public class GameEvents {

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    String msg = e.chatMessage().getPlainText();

    if (msg.equals("Let the games begin!")) {
      CubepanionManager m = Cubepanion.get().getManager();
      m.setInPreLobby(false);
      m.setGameStartTime(System.currentTimeMillis());

      Laby.fireEvent(new GameStartEvent(m.getDivision()));
      return;
    }

    if (msg.equals("You are now invincible for 10 seconds.")) {
      Laby.fireEvent(new PlayerRespawnEvent(true, SessionTracker.get().uuid()));
    }
  }

}
