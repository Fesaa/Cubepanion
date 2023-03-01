package org.cubecraftutilities.core.commands;


import net.labymod.api.client.chat.command.Command;
import org.cubecraftutilities.core.CCU;

public class FriendListCommand extends Command {

  private final CCU addon;

  public FriendListCommand(CCU addon) {
    super("friend", "friends", "f");

    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {

    if (arguments.length != 2) {
      return false;
    }

    if (!(arguments[0].equals("l") || arguments[0].equals("list")
     && arguments[1].equals("full") || arguments[1].equals("f"))) {
      return false;
    }

    this.addon.getManager().setRequestedFullFriendsList(true);
    this.addon.labyAPI().minecraft().chatExecutor().chat("/fl", false);
    return true;
  }
}
