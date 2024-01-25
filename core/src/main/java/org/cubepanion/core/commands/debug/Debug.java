package org.cubepanion.core.commands.debug;

import net.labymod.api.client.chat.command.Command;
import org.cubepanion.core.Cubepanion;

public class Debug extends Command {

  public Debug(Cubepanion addon) {
    super("cdebug");

    withSubCommand(new DivisionCommand(addon));
    withSubCommand(new MapCommand(addon));
    withSubCommand(new TeamColourCommand(addon));
    withSubCommand(new State(addon));
  }

  @Override
  public boolean execute(String s, String[] strings) {
    return false;
  }
}
