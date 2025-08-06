package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.GameStartEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PlayerRespawnEvent;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

public class GameEvents {

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    String msg = e.chatMessage().getPlainText();

    if (msg.equals("Let the games begin!")) {
      Cubepanion.get().getManager().onGameStart();
      return;
    }

    if (msg.equals("You are now invincible for 10 seconds.")) {
      Laby.fireEvent(new PlayerRespawnEvent(true, SessionTracker.get().uuid()));
    }
  }

}
