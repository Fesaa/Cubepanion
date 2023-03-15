package org.cubecraftutilities.core.commands;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.config.CCUconfig;
import org.cubecraftutilities.core.managers.CCUManager;
import org.cubecraftutilities.core.managers.submanagers.FriendTrackerManager;
import org.cubecraftutilities.core.utils.Colours;

public class OnlineFriendTrackerCommand extends Command {

  public OnlineFriendTrackerCommand() {
    super("tracker", "t");

    this.messagePrefix = CCU.get().prefix();
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
    Component reply = Component.newline();
    if (arguments.length == 1) {
      switch (arguments[0]) {
        case "track": {
          if (friendTrackerManager.getTracking().size() == 0) {
            reply = reply.append(
                Component.text("Currently not tracking anyone. Track someone with ", Colours.Primary)
                    .append(Component.text("/tracker track <username>", Colours.Secondary)
                        .clickEvent(ClickEvent.suggestCommand("/tracker track "))
                        .hoverEvent(HoverEvent.showText(Component.text("Prepare command")))));
            break;
          }

          reply = reply.append(Component.text("Currently tracking: ", Colours.Primary));
          for (String username : friendTrackerManager.getTracking()) {
            reply = reply
                .append(Component.text(username, Colours.Secondary)
                        .hoverEvent(HoverEvent.showText(Component.text("Prepare untrack")))
                        .clickEvent(ClickEvent.suggestCommand("/t untrack " + username)))
                .append(Component.text(",", Colours.Primary));
          }
          break;
        }
        case "untrack": {
          if (friendTrackerManager.getTracking().size() == 0) {
            reply = reply.append(Component.text("Currently not tracking anyone.", Colours.Primary));
            break;
          }

          reply = reply.append(Component.text("Click to untrack: ", Colours.Primary));
          for (String username : friendTrackerManager.getTracking()) {
            reply = reply
                .append(Component.text(username, Colours.Secondary)
                        .hoverEvent(HoverEvent.showText(Component.text("Click to untrack")))
                        .clickEvent(ClickEvent.runCommand("/t untrack " + username)))
                .append(Component.text(",", Colours.Primary));
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

    switch (arguments[0]) {
      case "track": {
        reply = reply.append(Component.text("Started tracking: ", Colours.Primary));
        for (int i = 1; i < arguments.length; i++) {
          friendTrackerManager.addTracking(arguments[i]);
          reply = reply.append(Component.text(arguments[i], Colours.Secondary)
              .append(Component.text(",", Colours.Primary)));
        }
        friendTrackerManager.forceUpdate();
        break;
      }
      case "untrack": {
        reply = reply.append(Component.text("Stopped tracking: ", Colours.Secondary));
        for (int i = 1; i < arguments.length; i++) {
          friendTrackerManager.unTrack(arguments[i]);
          reply = reply.append(Component.text(arguments[i], Colours.Primary)
              .append(Component.text(",", Colours.Secondary)));
        }
        break;
      }
      case "interval":
      case "i": {
        int interval;
        try {
          interval = Integer.parseInt(arguments[1]);
        } catch (NumberFormatException e) {
          this.displayMessage(Component.text("Second argument is not an integer.", Colours.Error));
          return true;
        }
        interval = Math.max(interval, 10);
        friendTrackerManager.setUpdateInterVal(interval);
        this.displayMessage(Component.text("Set update interval to: " + interval, Colours.Primary));
        return true;
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
    this.displayMessage(Component.text("Not a recognised option! /tracker (un)track/interval [username|int]", Colours.Error));
  }
}
