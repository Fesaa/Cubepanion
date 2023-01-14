package org.ccu.core.commands;

import com.google.inject.Inject;
import net.labymod.api.client.chat.command.Command;
import org.ccu.core.CCU;

public class EggWarsMapInfoCommand extends Command {

  private final CCU addon;

  @Inject
  private EggWarsMapInfoCommand(CCU addon) {
    super("eggwarsmap", "ewm");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    String mapName;
    if (arguments.length == 0) {
      mapName = this.addon.getManager().getMapName();
    } else {
      mapName = String.join(" ", arguments);
    }
    return this.addon.getManager().doEggWarsMapLayout(mapName, false);
  }
}
