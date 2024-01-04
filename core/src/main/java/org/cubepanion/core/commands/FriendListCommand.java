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

    if (isFullFriendList(prefix, arguments)) {
      Laby.fireEvent(new RequestEvent(RequestEvent.RequestType.FULL_FRIEND_LIST));
      this.addon.labyAPI().minecraft().chatExecutor().chat("/fl", false);
      return true;
    }
    return false;
  }

  private boolean isFullFriendList(String prefix, String[] arguments) {
      return switch (prefix) {
          case "friend", "friends", "f" -> listAndFull(arguments);
          case "fl" -> full(arguments);
          case "flf" -> true;
          default -> false;
      };
  }

  private boolean listAndFull(String[] arguments) {
    if (arguments.length != 2) {
      return false;
    }
    return arguments[0].equals("list") && arguments[1].equals("full");
  }

  private boolean full(String[] arguments) {
    return arguments.length == 1 && arguments[0].equals("full");
  }
}
