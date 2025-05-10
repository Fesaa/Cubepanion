package art.ameliah.laby.addons.cubepanion.core.commands;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.CommandSystemSubConfig;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import net.labymod.api.client.chat.command.Command;

public class GameMapInfoCommand extends Command {

  private final Cubepanion addon;

  public GameMapInfoCommand(Cubepanion addon) {
    super("gamemap", "gm");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    CubeGame game = this.addon.getManager().getDivision();
    if (!this.addon.getManager().onCubeCraft() || !CubepanionAPI.I().hasMaps(game)) {
      return false;
    }
    CommandSystemSubConfig config = this.addon.configuration().getCommandSystemSubConfig();

    if (!config.getGameMapInfoCommand().get() || !config.getEnabled().get()) {
      return false;
    }

    String mapName;
    if (arguments.length == 0) {
      mapName = this.addon.getManager().getMapName();
    } else {
      mapName = String.join(" ", arguments);
    }

    this.addon.getManager().getGameMapInfoManager().doGameMapLayout(mapName);
    return true;
  }
}
