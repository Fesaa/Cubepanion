package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.nametags.LevelTag;
import net.labymod.api.client.chat.command.SubCommand;

public class LevelsCommand extends SubCommand {

  private final Cubepanion addon;

  public LevelsCommand(Cubepanion addon) {
    super("levels");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }

    var sb = new StringBuilder();
    for (var entry : LevelTag.getLevels().entrySet()) {
      sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
    }

    this.displayMessage(sb.toString());

    return true;
  }
}
