package org.cubepanion.core.listener.misc;

import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.AutomationConfig;

public class Sounds {

  private final AutomationConfig config;

  public Sounds() {
    config = Cubepanion.get().configuration().getAutomationConfig();
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    Minecraft minecraft = Laby.labyAPI().minecraft();
    String msg = e.chatMessage().getPlainText();

    if (config.friendMessageSound().get()) {
      if (msg.matches("\\[Friend\\] ([a-zA-Z0-9_]{2,16}) -> Me : .*")) {
        minecraft.sounds().playSound(config.getFriendMessageSoundId(), 100, 1);
        return;
      }
    }
  }

}
