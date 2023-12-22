package org.cubepanion.core.commands;


import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.events.RequestEvent;

public class FriendListCommand extends Command {

  private final Cubepanion addon;

  public FriendListCommand(Cubepanion addon) {
    super("friend", "friends", "f", "fl", "flf");

    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }

    if ((
        arguments.length > 0 ?
            this.stringJoiner(arguments) :
            ""
    ).contains("full")
        || prefix.equals("flf")) {
      Laby.fireEvent(new RequestEvent(RequestEvent.RequestType.FULL_FRIEND_LIST));
      this.addon.labyAPI().minecraft().chatExecutor().chat("/fl", false);
      return true;
    }
    return false;

  }

  private String stringJoiner(String[] string) {
    StringBuilder out = new StringBuilder();
    for (String s : string) {
      out.append(" ").append(s);
    }
    return out.toString();
  }
}
