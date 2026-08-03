package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import java.util.Locale;

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

    var target = strings.length > 0 ? strings[0].toLowerCase(Locale.ROOT) : "";

    switch (target) {
      case "" -> {
        this.displayMessage("Reloading all API data...");
        this.displayMessage(Component.newline());
        CubepanionAPI.I().loadInitialData(true);
      }
      case "games" -> {
        this.displayMessage("Reloading games...");
        CubepanionAPI.I().loadGames(true);
      }
      case "chests" -> {
        this.displayMessage("Reloading chest locations...");
        CubepanionAPI.I().loadChestLocations(true);
      }
      case "maps" -> {
        this.displayMessage("Reloading game maps...");
        CubepanionAPI.I().loadGameMaps(true);
      }
      default -> {
        this.displayMessage("Unknown target: " + target + " (expected: games, chests, maps)");
        return true;
      }
    }

    return true;
  }
}
