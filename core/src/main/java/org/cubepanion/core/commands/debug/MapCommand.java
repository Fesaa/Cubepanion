package org.cubepanion.core.commands.debug;

import net.labymod.api.client.chat.command.SubCommand;
import org.cubepanion.core.Cubepanion;

public class MapCommand extends SubCommand {

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
