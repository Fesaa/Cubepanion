package org.cubecraftutilities.core.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.imp.GameStatsTracker;
import org.cubecraftutilities.core.config.subconfig.CommandSystemSubConfig;
import org.cubecraftutilities.core.config.subconfig.StatsTrackerSubConfig;
import org.cubecraftutilities.core.managers.CCUManager;
import org.cubecraftutilities.core.utils.Colours;

public class StatCommands extends Command {

  private final CCU addon;
  private final Pattern timeFormat = Pattern.compile("\\b(20[0-9]{2})-([1-9]|1[1-2])-(1[0-9]|2[0-9]|3[0-1]|[1-9])\\b");

  public StatCommands(CCU addon) {
    super("stats");

    this.addon = addon;
    this.messagePrefix = addon.prefix();
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    CommandSystemSubConfig commandSystemSubConfig = this.addon.configuration().getCommandSystemSubConfig();

    if (!commandSystemSubConfig.getStatsCommand().get() || !commandSystemSubConfig.getEnabled().get()) {
      return false;
    }

    if (arguments.length > 0
    && arguments[0].equalsIgnoreCase("help")) {
      this.helpCommand();
      return true;
    }


    CCUManager manager = this.addon.getManager();
    StatsTrackerSubConfig config = this.addon.configuration().getStatsTrackerSubConfig();
    HashMap<String, GameStatsTracker> allGameStatsTrackers = config.getGameStatsTrackers();

    String gameName = this.getGameString(arguments);

    GameStatsTracker gameStatsTracker;
    if (gameName == null) {
       gameStatsTracker = allGameStatsTrackers.get(manager.getDivisionName());
    } else {
      gameStatsTracker = allGameStatsTrackers.get(gameName);
    }

    if (gameStatsTracker == null) {
      this.displayMessage(
          Component.text("Could not find the specified game. Possible options are ", Colours.Error)
              .hoverEvent(HoverEvent.showText(
                  Component.text("Stats Tracking has to be enabled in the addon setting for this command to display data.", Colours.Hover)
              ))
          .append(this.gamesList().color(Colours.Error)));
      return false;
    }

    if (gameName != null) {
      arguments = this.removeGameNameFromArguments(arguments, gameName);
    }

    switch (arguments.length) {
      case 0: {
        this.displayMessage(gameStatsTracker.getDisplayComponent());
        return true;
      }
      case 1: {
        if (this.timeFormat.matcher(arguments[0]).matches()) {
          GameStatsTracker snapShot = gameStatsTracker.getHistoricalData(arguments[0]);
          if (snapShot == null) {
            this.displayMessage(Component.text("No stats for " + gameStatsTracker.getGame() + " on " + arguments[0], Colours.Error));
          } else {
            this.displayMessage(snapShot.getDisplayComponent());
          }
        } else {
          this.displayMessage(gameStatsTracker.getUserStatsDisplayComponent(arguments[0]));
        }
        return true;
      }
      case 2: {

        String date;
        String userName;

        if (this.timeFormat.matcher(arguments[0]).matches()) {
          date = arguments[0];
          userName = arguments[1];
        } else if (this.timeFormat.matcher(arguments[1]).matches()) {
          date = arguments[1];
          userName = arguments[0];
        } else {
          break;
        }

        GameStatsTracker snapShot = gameStatsTracker.getHistoricalData(date);
        if (snapShot == null) {
          this.displayMessage(Component.text("No stats for " + gameStatsTracker.getGame() + " on " + date, Colours.Error));
        } else {
          this.displayMessage(snapShot.getUserStatsDisplayComponent(userName));
        }
      }
    }
    return false;
  }

  private void helpCommand() {
    Component helpComponent = Component.text("------- Enhanced Stats Commands -------", Colours.Title)
        .append(Component.text("\n/stats <game>", Colours.Primary)
            .clickEvent(ClickEvent.suggestCommand("/stats ")))
        .append(Component.text(" Displays your stats in a game.", Colours.Secondary))
        .append(Component.text("\n/stats <game> <username>", Colours.Primary)
            .clickEvent(ClickEvent.suggestCommand("/stats ")))
        .append(Component.text(" Displays your kills on and deaths by a player.", Colours.Secondary))
        .append(Component.text("\n/stats <game> <YYYY-MM-DD>", Colours.Primary)
            .clickEvent(ClickEvent.suggestCommand("/stats ")))
        .append(Component.text(" Displays your stats in a game on a certain day (Omit real 0's).", Colours.Secondary))
        .append(Component.text("\n/stats <game> <YYYY-MM-DD> <username>", Colours.Primary)
            .clickEvent(ClickEvent.suggestCommand("/stats "))
            .hoverEvent(HoverEvent.showText(Component.text("  This has to be enabled in settings.", Colours.Hover))))
        .append(Component.text(" Displays your stats in a game on a certain day with a player (Omit real 0's).", Colours.Secondary)
            .hoverEvent(HoverEvent.showText(Component.text("  This has to be enabled in settings.", Colours.Hover))))
        .append(Component.text("\n/stats help", Colours.Primary)
            .clickEvent(ClickEvent.suggestCommand("/stats help")))
        .append(Component.text(" Displays this message.", Colours.Secondary))
        .append(Component.text("\n\nGames tracked: ", Colours.Primary))
        .append(this.gamesList().color(Colours.Secondary));

    this.displayMessage(helpComponent);
  }

  private Component gamesList() {
    Component comp = Component.empty();
    Set<String> keySet = this.addon.configuration().getStatsTrackerSubConfig().getGameStatsTrackers().keySet();
    List<String> keys = new ArrayList<>(keySet);
    for (int i = 0; i < keys.size(); i++) {
      String game = keys.get(i);
      comp = comp
          .append(Component.text(game)
              .clickEvent(ClickEvent.suggestCommand("/stats " + game + " ")));
      if (i != keys.size() - 1) {
        comp = comp.append(Component.text(", "));
      }
    }
    return comp;
  }

  private String[] removeGameNameFromArguments(String[] arguments, String gameName) {
    String[] gameComponents = gameName.split(" ");

    for (String comp : gameComponents) {
      arguments = this.removeFromArguments(arguments, comp);
    }
    return arguments;
  }

  private String[] removeFromArguments(String[] arguments, String member) {
    String[] newArguments = new String[arguments.length - 1];

    int j = 0;
    for (String argument : arguments) {
      if (!argument.equals(member)) {
        newArguments[j] = argument;
        j++;
      }
    }
    return newArguments;
  }

  private String getGameString(String[] arguments) {
    String game = "";

    for (String arg : arguments) {
      game = (game + " " + arg).trim();

      GameStatsTracker tracker = this.addon.configuration().getStatsTrackerSubConfig().getGameStatsTrackers()
          .get(game);

      if (tracker != null) {
        return game;
      }
    }

    return null;
  }
}
