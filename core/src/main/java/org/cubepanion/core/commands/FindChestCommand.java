package org.cubepanion.core.commands;

import net.labymod.api.client.chat.command.Command;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.versionlinkers.ChestFinderLink;

public class FindChestCommand extends Command {

  private final Cubepanion addon;
  private final CubepanionManager manager;
  private final ChestFinderLink chestFinderLink;

  public FindChestCommand(Cubepanion addon, ChestFinderLink chestFinderLink) {
    super("findchest", "chests", "fc");
    this.addon = addon;
    this.manager = addon.getManager();
    this.chestFinderLink = chestFinderLink;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.manager.onCubeCraft()
        || !this.manager.getDivision().equals(CubeGame.LOBBY)
        || this.chestFinderLink == null
        || !this.addon.configuration().getCommandSystemSubConfig().getChestFinderCommand().get()) {
      return false;
    }
    chestFinderLink.displayLocations();
    return true;
  }
}
