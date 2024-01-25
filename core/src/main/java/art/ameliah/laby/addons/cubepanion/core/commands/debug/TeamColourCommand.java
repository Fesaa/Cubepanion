package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import net.labymod.api.client.chat.command.SubCommand;
import art.ameliah.laby.addons.cubepanion.core.Cubepanion;

public class TeamColourCommand extends SubCommand {

  private final Cubepanion addon;

  public TeamColourCommand(Cubepanion addon) {
    super("teamcolour", "tm");
    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }
    this.displayMessage(this.addon.getManager().getTeamColour());
    return true;
  }
}
