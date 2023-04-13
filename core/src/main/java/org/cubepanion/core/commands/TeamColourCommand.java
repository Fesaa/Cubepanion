package org.cubepanion.core.commands;

import net.labymod.api.client.chat.command.Command;
import org.cubepanion.core.Cubepanion;

public class TeamColourCommand extends Command {

  private final Cubepanion addon;

  public TeamColourCommand(Cubepanion addon) {
    super("teamcolour", "tm");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }
    this.displayMessage(this.addon.getManager().getTeamColour());
    return true;
  }
}
