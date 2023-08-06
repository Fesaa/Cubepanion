package org.cubepanion.core.commands;

import net.labymod.api.client.chat.command.Command;
import org.cubepanion.core.Cubepanion;

public class MapCommand extends Command {

  private final Cubepanion addon;

  public MapCommand(Cubepanion addon) {
    super("map");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }
    this.displayMessage(this.addon.getManager().getMapName());
    return true;
  }
}
