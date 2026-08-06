package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.PrintDebugRequestEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PrintDebugRequestEvent.Receiver;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;

public class PrintDebugRequestsCommand extends SubCommand {

  private final Cubepanion addon;

  public PrintDebugRequestsCommand(Cubepanion addon) {
    super("print");

    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!addon.getManager().onCubeCraft()) {
      return false;
    }

    if (arguments.length < 1) {
      return false;
    }

    try {
      var receiver = Receiver.valueOf(arguments[0]);
      Laby.fireEvent(new PrintDebugRequestEvent(receiver));
    } catch (IllegalArgumentException ignored) {
      this.sendMessage("No receiver " + arguments[0] + "found");
    }

    return true;
  }
}
