package org.cubecraftutilities.core.commands;

import java.util.Set;
import java.util.function.Function;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.CCUconfig;
import org.cubecraftutilities.core.managers.CCUManager;
import org.cubecraftutilities.core.managers.submanagers.FriendTrackerManager;
import org.cubecraftutilities.core.utils.Colours;
import org.cubecraftutilities.core.utils.I18nNamespaces;

public class OnlineFriendTrackerCommand extends Command {

  private final Function<String, String> keyGetter;
  private final Function<String, Component> componentGetter;
  private final Function<String, Component> componentGetterSucces;
  private final Function<String, Component> componentGetterError;

  public OnlineFriendTrackerCommand() {
    super("tracker", "t");

    this.messagePrefix = CCU.get().prefix();
    this.keyGetter = I18nNamespaces.commandNameSpaceMaker("OnlineFriendTrackerCommand");
    this.componentGetter = I18nNamespaces.commandNamespaceTransformer("OnlineFriendTrackerCommand");
    this.componentGetterSucces = I18nNamespaces.commandNamespaceTransformer("OnlineFriendTrackerCommand.succes");
    this.componentGetterError = I18nNamespaces.commandNamespaceTransformer("OnlineFriendTrackerCommand.error");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    CCUManager manager = CCU.get().getManager();
    CCUconfig config = CCU.get().configuration();
    if (!config.getCommandSystemSubConfig().getFriendsTrackerCommand().get()
    || !config.getCommandSystemSubConfig().getEnabled().get()
    || !manager.onCubeCraft()
    || arguments.length == 0) {
      return false;
    }

    FriendTrackerManager friendTrackerManager = manager.getFriendTrackerManager();
    Component reply = Component.empty();
    if (arguments.length == 1) {
      switch (arguments[0]) {
        case "track": {
          if (friendTrackerManager.getTracking().size() == 0) {
            reply = reply.append(
                this.componentGetterError.apply("notTrackingAnyOneSuggestion").color(Colours.Primary)
                    .append(Component.text("/tracker track <username>", Colours.Secondary)
                        .clickEvent(ClickEvent.suggestCommand("/tracker track "))));
            break;
          }

          reply = reply.append(this.componentGetterSucces.apply("currentlyTracking").color(Colours.Primary));
          for (String username : friendTrackerManager.getTracking()) {
            reply = reply
                .append(Component.text(username, Colours.Secondary)
                        .clickEvent(ClickEvent.suggestCommand("/t untrack " + username)))
                .append(Component.text(",", Colours.Primary));
          }
          break;
        }
        case "untrack": {
          if (friendTrackerManager.getTracking().size() == 0) {
            reply = reply.append(this.componentGetterError.apply("notTrackingAnyOne").color(Colours.Error));
            break;
          }

          reply = reply.append(this.componentGetterSucces.apply("clickToUntrack").color(Colours.Primary));
          Set<String> tracking = friendTrackerManager.getTracking();
          int size = tracking.size();
          int i = 0;
          for (String username : tracking) {
            reply = reply
                .append(Component.text(username, Colours.Secondary)
                        .clickEvent(ClickEvent.runCommand("/t untrack " + username)));
            if (i != size) {
              reply = reply .append(Component.text(",", Colours.Primary));
            }
            i++;
          }
          break;
        }
        case "interval": {
          reply = reply.append(
              Component.translatable(
                  this.keyGetter.apply("succes.intervalLengthResponse"),
                  Colours.Primary,
                  Component.text(
                      friendTrackerManager.getUpdateInterVal(),
                      Colours.Secondary)
              ));
          break;
        }
        default: {
          this.notARecognisedOption();
          return true;
        }
      }
      this.displayMessage(reply);
      return true;
    }

    switch (arguments[0]) {
      case "track": {
        reply = reply.append(this.componentGetterSucces.apply("startedTracking").color(Colours.Primary));
        for (int i = 1; i < arguments.length; i++) {
          friendTrackerManager.addTracking(arguments[i]);
          reply = reply.append(Component.text(arguments[i], Colours.Secondary));
          if (i != arguments.length - 1) {
            reply = reply.append(Component.text(",", Colours.Primary));
          }
        }
        friendTrackerManager.forceUpdate();
        break;
      }
      case "untrack": {
        reply = reply.append(this.componentGetterSucces.apply("stoppedTracking").color(Colours.Primary));
        for (int i = 1; i < arguments.length; i++) {
          friendTrackerManager.unTrack(arguments[i]);
          reply = reply.append(Component.text(arguments[i], Colours.Secondary));
          if (i != arguments.length - 1) {
            reply = reply.append(Component.text(",", Colours.Primary));
          }
        }
        break;
      }
      case "interval":
      case "i": {
        int interval;
        try {
          interval = Integer.parseInt(arguments[1]);
          interval = Math.max(interval, 10);
          friendTrackerManager.setUpdateInterVal(interval);
          reply = Component.translatable(
              this.keyGetter.apply("succes.setIntervalTo"),
              Colours.Primary,
              Component.text(
                  friendTrackerManager.getUpdateInterVal(),
                  Colours.Secondary));
        } catch (NumberFormatException e) {
          reply = this.componentGetterError.apply("notAnInteger").color(Colours.Error);
        }
        break;
      }
      default: {
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
