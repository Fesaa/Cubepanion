package art.ameliah.laby.addons.cubepanion.core.listener.misc;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.AutomationConfig;
import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

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
