package org.cubepanion.core.commands;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.CommandSystemSubConfig;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.utils.I18nNamespaces;

public class EggWarsMapInfoCommand extends Command {

  private final Cubepanion addon;

  public EggWarsMapInfoCommand(Cubepanion addon) {
    super("eggwarsmap", "ewm");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft() && !this.addon.getManager().getDivision().equals(CubeGame.TEAM_EGGWARS)) {
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
