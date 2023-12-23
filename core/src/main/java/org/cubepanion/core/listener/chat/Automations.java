package org.cubepanion.core.listener.chat;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.managers.CubepanionManager;

public class Automations {

  private final Cubepanion addon;
  private final CubepanionManager manager;
  private final Pattern playerElimination = Pattern.compile(
      "([a-zA-Z0-9_]{2,16}) has been eliminated from the game\\.");
  private final Pattern WhereAmIOutPut = Pattern.compile(
      "You are on proxy: (\\w{0,2}bungeecord\\d{1,3})\\nYou are on server: (.{5})");

  public Automations(Cubepanion addon) {
    this.addon = addon;
    this.manager = addon.getManager();
  }

  @Subscribe
  public void onChatReceiveEvent(ChatReceiveEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }
    String msg = e.chatMessage().getPlainText();

    // Bungee & serverid matcher
    Matcher whereAmIMatcher = this.WhereAmIOutPut.matcher(msg);
    if (whereAmIMatcher.matches()) {
      this.manager.setBungeecord(whereAmIMatcher.group(1));
      this.manager.setServerID(whereAmIMatcher.group(2));
      return;
    }
  }
}
