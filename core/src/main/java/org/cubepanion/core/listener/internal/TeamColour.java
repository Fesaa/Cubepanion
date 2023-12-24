package org.cubepanion.core.listener.internal;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import org.cubepanion.core.Cubepanion;

public class TeamColour {

  private final Pattern EggWarsTeamJoin = Pattern.compile("You have joined .{1,30} team\\.");


  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    String msg = e.chatMessage().getPlainText();

    Matcher teamColourMatcher = this.EggWarsTeamJoin.matcher(msg);
    if (teamColourMatcher.matches()) {
      List<Component> children = e.chatMessage().component().getChildren();
      if (children.isEmpty()) {
        Cubepanion.get().getManager().setTeamColour("yellow");
      } else {
        Cubepanion.get().getManager().setTeamColour(children.get(0).getColor().toString());
      }
    }
  }

}
