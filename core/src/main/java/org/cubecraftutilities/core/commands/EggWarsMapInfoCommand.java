package org.cubecraftutilities.core.commands;

import net.labymod.api.client.chat.command.Command;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.subconfig.CommandSystemSubConfig;

public class EggWarsMapInfoCommand extends Command {

  private final CCU addon;

  public EggWarsMapInfoCommand(CCU addon) {
    super("eggwarsmap", "ewm");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    CommandSystemSubConfig config = this.addon.configuration().getCommandSystemSubConfig();

    if (!config.getEggWarsMapInfoCommand().get() || !config.getEnabled().get()) {
      return false;
    }

    String mapName;
    if (arguments.length == 0) {
      mapName = this.addon.getManager().getMapName();
    } else {
      mapName = String.join(" ", arguments);
    }
    return this.addon.getManager().getEggWarsMapInfoManager().doEggWarsMapLayout(mapName, false);
  }
}
