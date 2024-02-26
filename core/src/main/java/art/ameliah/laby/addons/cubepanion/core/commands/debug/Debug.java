package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import net.labymod.api.client.chat.command.Command;

public class Debug extends Command {

  public Debug(Cubepanion addon) {
    super("cdebug");

    withSubCommand(new DivisionCommand(addon));
    withSubCommand(new MapCommand(addon));
    withSubCommand(new TeamColourCommand(addon));
    withSubCommand(new State(addon));
  }

  @Override
  public boolean execute(String s, String[] strings) {
    return false;
  }
}
