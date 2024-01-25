package org.cubepanion.core.commands.debug;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.SubCommand;
import org.cubepanion.core.Cubepanion;

public class DivisionCommand extends SubCommand {

  private final Cubepanion addon;

  public DivisionCommand(Cubepanion addon) {
    super("division");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }
    this.displayMessage(this.addon.getManager().getDivision().getString());
    return true;
  }
}
