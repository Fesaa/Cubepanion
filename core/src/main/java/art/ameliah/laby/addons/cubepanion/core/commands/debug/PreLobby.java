package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import net.labymod.api.client.chat.command.SubCommand;

public class PreLobby extends SubCommand {

  private final Cubepanion addon;

  protected PreLobby(Cubepanion addon) {
    super("prelobby", "pl");

    this.addon = addon;
  }

  @Override
  public boolean execute(String s, String[] strings) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }

    this.displayMessage(""+this.addon.getManager().isInPreGameState());
    return true;
  }
}
