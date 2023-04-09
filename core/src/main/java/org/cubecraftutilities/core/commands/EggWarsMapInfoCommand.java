package org.cubecraftutilities.core.commands;

import java.util.function.Function;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.subconfig.CommandSystemSubConfig;
import org.cubecraftutilities.core.utils.Colours;
import org.cubecraftutilities.core.utils.I18nNamespaces;

public class EggWarsMapInfoCommand extends Command {

  private final CCU addon;

  public EggWarsMapInfoCommand(CCU addon) {
    super("eggwarsmap", "ewm");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }
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

    boolean check = this.addon.getManager().getEggWarsMapInfoManager().doEggWarsMapLayout(mapName, false);
    if (!check) {
      this.displayMessage(
          Component.translatable()
          .key(I18nNamespaces.commandNamespace + "EggWarsMapInfoCommand." + "mapNotFound")
          .argument(Component.text(mapName))
          .build()
          .color(Colours.Error));
    }
    return true;
  }
}
