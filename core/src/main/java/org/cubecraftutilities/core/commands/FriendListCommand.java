package org.cubecraftutilities.core.commands;


import net.labymod.api.client.chat.command.Command;
import org.cubecraftutilities.core.CCU;

public class FriendListCommand extends Command {

  private final CCU addon;

  public FriendListCommand(CCU addon) {
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
      this.addon.getManager().setRequestedFullFriendsList(true);
      this.addon.labyAPI().minecraft().chatExecutor().chat("/fl", false);
      return true;
    }
    return false;

  }

  private String stringJoiner(String[] string) {
    StringBuilder out = new StringBuilder();
    for (String s: string) {
      out.append(" ").append(s);
    }
    return out.toString();
  }
}
