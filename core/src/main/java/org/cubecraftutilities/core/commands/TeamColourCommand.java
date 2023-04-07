package org.cubecraftutilities.core.commands;

import net.labymod.api.client.chat.command.Command;
import org.cubecraftutilities.core.CCU;

public class TeamColourCommand extends Command {

  private final CCU addon;

  public TeamColourCommand(CCU addon) {
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
