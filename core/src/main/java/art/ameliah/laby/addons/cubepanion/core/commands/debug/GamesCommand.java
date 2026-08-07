package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.external.Game;
import net.labymod.api.client.chat.command.SubCommand;

public class GamesCommand extends SubCommand {

  private final Cubepanion addon;

  public GamesCommand(Cubepanion addon) {
    super("games");

    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!addon.getManager().onCubeCraft()) {
      return false;
    }

    var sb = new StringBuilder();

    for (var game : CubepanionAPI.I().getGamesMap().values()) {
      sb.append(game.displayName()).append(": ")
          .append(String.join(",", game.aliases()))
          .append(",").append(game.id())
          .append("\n");
    }

    this.displayMessage(sb.toString());
    return true;
  }
}
