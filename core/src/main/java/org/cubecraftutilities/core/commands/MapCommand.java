package org.cubecraftutilities.core.commands;

import net.labymod.api.client.chat.command.Command;
import org.cubecraftutilities.core.CCU;

public class MapCommand extends Command {

  private final CCU addon;

  public MapCommand(CCU addon) {
    super("map" );
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }
    this.displayMessage(this.addon.getManager().getDivisionName());
    return true;
  }
}
