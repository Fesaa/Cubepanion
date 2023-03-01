package org.cubecraftutilities.core.commands;


import net.labymod.api.client.chat.command.Command;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.utils.Utils;

public class FriendListCommand extends Command {

  private final CCU addon;

  public FriendListCommand(CCU addon) {
    super("friend", "friends", "f", "fl", "flf");

    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if ((
        arguments.length > 0 ?
            Utils.stringJoiner(arguments, " ") :
            ""
    ).contains("full")
    || prefix.equals("flf")) {
      this.addon.getManager().setRequestedFullFriendsList(true);
      this.addon.labyAPI().minecraft().chatExecutor().chat("/fl", false);
      return true;
    }
    return false;

  }
}
