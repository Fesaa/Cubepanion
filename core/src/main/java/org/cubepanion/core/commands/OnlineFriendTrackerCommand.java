package org.cubepanion.core.commands;

import java.util.Set;
import java.util.function.Function;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.format.TextDecoration;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.CubepanionConfig;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.managers.submanagers.FriendTrackerManager;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.I18nNamespaces;

public class OnlineFriendTrackerCommand extends Command {

  private final Function<String, String> keyGetter;
  private final Function<String, Component> componentGetterSuccess;
  private final Function<String, Component> componentGetterError;

  private final Component help;

  public OnlineFriendTrackerCommand() {
    super("friendTracker", "ft");

    this.messagePrefix = Cubepanion.get().prefix();
    this.keyGetter = I18nNamespaces.commandNameSpaceMaker("OnlineFriendTrackerCommand");
    this.componentGetterSuccess = I18nNamespaces.commandNamespaceTransformer(
        "OnlineFriendTrackerCommand.success");
    this.componentGetterError = I18nNamespaces.commandNamespaceTransformer(
        "OnlineFriendTrackerCommand.error");
    Function<String, Component> helpComponent = I18nNamespaces.commandNamespaceTransformer(
        "OnlineFriendTrackerCommand.helpCommand");

    this.help = helpComponent.apply("title")
        .color(Colours.Title)
        .append(helpComponent.apply("description").color(Colours.Secondary)
            .decorate(TextDecoration.ITALIC))
        .append(Component.text("\n/friendTracker track [names*]", Colours.Primary)
            .undecorate(TextDecoration.ITALIC))
        .append(helpComponent.apply("track").color(Colours.Secondary))
        .append(Component.text("\n/friendTracker untrack [names*]", Colours.Primary))
        .append(helpComponent.apply("untrack").color(Colours.Secondary))
        .append(Component.text("\n/friendTracker interval [int]", Colours.Primary))
        .append(helpComponent.apply("interval").color(Colours.Secondary));
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    CubepanionManager manager = Cubepanion.get().getManager();
    CubepanionConfig config = Cubepanion.get().configuration();
    if (!config.getCommandSystemSubConfig().getFriendsTrackerCommand().get()
        || !config.getCommandSystemSubConfig().getEnabled().get()
        || !manager.onCubeCraft()) {
      return false;
    }

    if (arguments.length == 0) {
      this.displayMessage(this.help);
      return true;
    }

    FriendTrackerManager friendTrackerManager = manager.getFriendTrackerManager();
    Component reply = Component.empty();
    if (arguments.length == 1) {
      switch (arguments[0]) {
        case "help" -> reply = reply.append(this.help);
        case "track" -> {
          if (friendTrackerManager.getTracking().isEmpty()) {
            reply = reply.append(
                this.componentGetterError.apply("notTrackingAnyOneSuggestion")
                    .color(Colours.Primary)
                    .append(Component.text("/friendTracker track [username*]", Colours.Secondary)
                        .clickEvent(ClickEvent.suggestCommand("/friendTracker track "))));
            break;
          }

          reply = reply.append(
              this.componentGetterSuccess.apply("currentlyTracking").color(Colours.Primary));
          for (String username : friendTrackerManager.getTracking()) {
            reply = reply
                .append(Component.text(username, Colours.Secondary)
                    .clickEvent(ClickEvent.suggestCommand("/friendTracker untrack " + username)))
                .append(Component.text(", ", Colours.Primary));
          }
        }
        case "untrack" -> {
          if (friendTrackerManager.getTracking().isEmpty()) {
            reply = reply.append(
                this.componentGetterError.apply("notTrackingAnyOne").color(Colours.Error));
            break;
          }

          reply = reply.append(
              this.componentGetterSuccess.apply("clickToUntrack").color(Colours.Primary));
          Set<String> tracking = friendTrackerManager.getTracking();
          int size = tracking.size();
          int i = 0;
          for (String username : tracking) {
            reply = reply
                .append(Component.text(username, Colours.Secondary)
                    .clickEvent(ClickEvent.runCommand("/friendTracker untrack " + username)));
            if (i != size) {
              reply = reply.append(Component.text(", ", Colours.Primary));
            }
            i++;
          }
        }
        case "interval" -> reply = reply.append(
            Component.translatable(
                this.keyGetter.apply("success.intervalLengthResponse"),
                Colours.Primary,
                Component.text(
                    friendTrackerManager.getUpdateInterVal(),
                    Colours.Secondary)
            ));
        default -> {
          this.notARecognisedOption();
          return true;
        }
      }
      this.displayMessage(reply);
      return true;
    }

    switch (arguments[0]) {
      case "track" -> {
        reply = reply.append(
            this.componentGetterSuccess.apply("startedTracking").color(Colours.Primary));
        for (int i = 1; i < arguments.length; i++) {
          friendTrackerManager.addTracking(arguments[i]);
          reply = reply.append(Component.text(arguments[i], Colours.Secondary));
          if (i != arguments.length - 1) {
            reply = reply.append(Component.text(",", Colours.Primary));
          }
        }
        friendTrackerManager.forceUpdate();
      }
      case "untrack" -> {
        reply = reply.append(
            this.componentGetterSuccess.apply("stoppedTracking").color(Colours.Primary));
        for (int i = 1; i < arguments.length; i++) {
          friendTrackerManager.unTrack(arguments[i]);
          reply = reply.append(Component.text(arguments[i], Colours.Secondary));
          if (i != arguments.length - 1) {
            reply = reply.append(Component.text(",", Colours.Primary));
          }
        }
      }
      case "interval", "i" -> {
        int interval;
        try {
          interval = Integer.parseInt(arguments[1]);
          interval = Math.max(interval, 10);
          friendTrackerManager.setUpdateInterVal(interval);
          reply = Component.translatable(
              this.keyGetter.apply("success.setIntervalTo"),
              Colours.Primary,
              Component.text(
                  friendTrackerManager.getUpdateInterVal(),
                  Colours.Secondary));
        } catch (NumberFormatException e) {
          reply = this.componentGetterError.apply("notAnInteger").color(Colours.Error);
        }
      }
      default -> {
        this.notARecognisedOption();
        return true;
      }
    }
    this.displayMessage(reply);
    return true;
  }

  private void notARecognisedOption() {
    this.displayMessage(this.componentGetterError.apply("notAValidCommand").color(Colours.Error));
  }
}
