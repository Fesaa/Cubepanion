package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import net.labymod.api.client.chat.command.SubCommand;

public class ResetConnectionTries extends SubCommand {

  private final Cubepanion addon;

  protected ResetConnectionTries(Cubepanion addon) {
    super("resetconnectiontries", "rc", "rct");

    this.addon = addon;
  }

  @Override
  public boolean execute(String s, String[] strings) {
    if (!this.addon.getManager().onCubeCraft()) {
      return false;
    }

    this.addon.getSocket().setConnectTries(0);
    return true;
  }
}
