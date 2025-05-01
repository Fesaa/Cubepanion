package art.ameliah.laby.addons.cubepanion.core.commands.debug;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.weave.ChestAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.GameMapAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.GamesAPI;
import art.ameliah.laby.addons.cubepanion.core.weave.LeaderboardAPI;
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
    ChestAPI.getInstance().loadChestLocations((chestLocations -> {
      if (chestLocations == null) {
        return;
      }
      this.displayMessage("Loaded " + chestLocations.length + " chest locations.");
    }));
    ChestAPI.getInstance().loadSeason(seasons -> {
      if (seasons == null) {
        return;
      }
      this.displayMessage("Loaded " + seasons.length + " seasons.");
    });
    GamesAPI.I().loadGames(leaderboards -> {
      if (leaderboards == null) {
        return;
      }
      this.displayMessage("Loaded " + leaderboards.length + " games.");
    });
    GameMapAPI.getInstance().loadMaps(maps -> {
      if (maps == null) {
        return;
      }
      this.displayMessage("Loaded " + maps.length + " maps.");
    });

    return true;
  }
}
