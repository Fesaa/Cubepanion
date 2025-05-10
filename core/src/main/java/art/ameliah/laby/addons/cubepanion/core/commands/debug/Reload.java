package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;

public class Reload extends SubCommand {

  private final Cubepanion addon;

  protected Reload(Cubepanion addon) {
    super("reload", "r");

    this.addon = addon;
  }

  @Override
  public boolean execute(String s, String[] strings) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }

    this.displayMessage("Reloading all API data...");
    this.displayMessage(Component.newline());
    // TODO: Print useful output
    CubepanionAPI.I().loadInitialData();

    return true;
  }
}
