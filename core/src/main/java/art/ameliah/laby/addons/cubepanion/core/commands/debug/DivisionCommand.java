package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import net.labymod.api.client.chat.command.SubCommand;

public class DivisionCommand extends SubCommand {

  private final Cubepanion addon;

  public DivisionCommand(Cubepanion addon) {
    super("division");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }
    this.displayMessage(this.addon.getManager().getDivision().getString());
    return true;
  }
}
