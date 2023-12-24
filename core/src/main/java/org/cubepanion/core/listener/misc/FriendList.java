package org.cubepanion.core.listener.misc;

import java.util.regex.Pattern;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.QOLConfig;
import org.cubepanion.core.events.RequestEvent;

public class FriendList {

  private final QOLConfig config;
  private final Pattern FriendListTop = Pattern.compile(
      "------- Friends \\(\\d{1,10}\\/\\d{1,10}\\) -------");
  private final Pattern FriendListOffline = Pattern.compile(
      "^(?:[a-zA-Z0-9_]{2,16}, )*[a-zA-Z0-9_]{2,16}$");
  private boolean friendListBeingSend = false;
  private boolean hasRequested = false;

  public FriendList() {
    config = Cubepanion.get().configuration().getQolConfig();
  }

  @Subscribe
  public void onRequest(RequestEvent e) {
    if (e.getType().equals(RequestEvent.RequestType.FULL_FRIEND_LIST)) {
      hasRequested = true;
    }
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    String msg = e.chatMessage().getPlainText();

    if (config.getShortFriendsList().get()) {
      if (this.FriendListTop.matcher(msg).matches()) {
        this.friendListBeingSend = true;
        return;
      }

      if (this.friendListBeingSend) {
        if (msg.equals("Offline: ") && !hasRequested) {
          e.setCancelled(true);
          return;
        }

        if (this.FriendListOffline.matcher(msg).matches()) {
          this.friendListBeingSend = false;
          if (hasRequested) {
            hasRequested = false;
          } else {
            e.setCancelled(true);
          }
        }
      }
    }
  }

}
