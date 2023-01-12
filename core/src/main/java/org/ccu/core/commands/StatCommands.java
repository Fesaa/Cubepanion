package org.ccu.core.commands;

import com.google.inject.Inject;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.labymod.api.client.chat.command.Command;
import org.ccu.core.CCU;
import org.ccu.core.config.imp.GameStatsTracker;
import org.ccu.core.config.internal.CCUinternalConfig;

public class StatCommands extends Command {

  private final CCU addon;
  private final Pattern timeFormat = Pattern.compile("\\b(20[0-9]{2})-([1-9]|1[1-2])-(1[0-9]|2[0-9]|3[0-1]|[1-9])\\b");

  @Inject
  private StatCommands(CCU addon) {
    super("stats");

    this.addon = addon;
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {

    if (!CCUinternalConfig.serverIP.equals("play.cubecraft.net")) {
      return false;
    }

    GameStatsTracker gameStatsTracker = this.addon.configuration().getStatsTrackerSubConfig().getGameStatsTrackers().get(CCUinternalConfig.name);

    if (gameStatsTracker == null) {

      GameStatsTracker gameStatsTrackerFromArguments = this.addon.configuration().getStatsTrackerSubConfig().getGameStatsTrackers().get(String.join(" ", arguments));

      if (gameStatsTrackerFromArguments != null) {
        this.displayMessage(gameStatsTrackerFromArguments.getDisplayComponent(this.addon));
        
        return true;
      }

      return false;
    }

    if (arguments.length == 0) {
      this.displayMessage(gameStatsTracker.getDisplayComponent(this.addon));

      return true;
    } else if (arguments.length == 1) {

      if (this.timeFormat.matcher(arguments[0]).matches()) {
        GameStatsTracker snapShot = gameStatsTracker.getHistoricalData(arguments[0]);
        this.displayMessage(snapShot.getDisplayComponent(this.addon));
      } else {
        this.displayMessage(gameStatsTracker.getUserStatsDisplayComponent(this.addon, arguments[0]));
      }

      return true;
    } else if (arguments.length == 2) {

      if (this.timeFormat.matcher(arguments[0]).matches()) {
        GameStatsTracker snapShot = gameStatsTracker.getHistoricalData(arguments[0]);
        this.displayMessage(snapShot.getUserStatsDisplayComponent(this.addon, arguments[1]));
      } else if (this.timeFormat.matcher(arguments[1]).matches()) {
        GameStatsTracker snapShot = gameStatsTracker.getHistoricalData(arguments[1]);
        this.displayMessage(snapShot.getUserStatsDisplayComponent(this.addon, arguments[0]));
      } else {
        Component errorComponent = Component.empty()
            .append(this.addon.prefix())
            .append(Component.text("Unknown request! Layout: ", NamedTextColor.RED))
            .append(Component.text("\n    •/stats YYYY-MM-DD <user name>", NamedTextColor.GRAY))
            .append(Component.text("\n    •/stats <user name> YYYY-MM-DD", NamedTextColor.GRAY))
            .append(Component.text("\n    -Date should not include trailing 0's.", NamedTextColor.GRAY))
            .append(Component.text("\n    -Ex: 2023-1-1 for First of January 2023.", NamedTextColor.GRAY));
        this.displayMessage(errorComponent);
      }

      return true;
    } else {
      return false;
    }
  }
}
