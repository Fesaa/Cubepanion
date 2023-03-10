package org.cubecraftutilities.core.commands;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.Colours;
import org.cubecraftutilities.core.config.submanagers.FriendTrackerManager;

public class OnlineFriendTrackerCommand extends Command {

  public OnlineFriendTrackerCommand() {
    super("tracker", "t");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!CCU.get().getManager().onCubeCraft()) {
      return false;
    }

    if (arguments.length == 0) {
      return false;
    }

    FriendTrackerManager friendTrackerManager = CCU.get().getManager().getFriendTrackerManager();

    if (arguments.length == 1) {
      switch (arguments[0]) {
        case "track": {
          Component reply = Component.text("Currently tracking: ", Colours.Secondary);
          for (String username : friendTrackerManager.getTracking()) {
            reply = reply
                .append(
                    Component.text(username, Colours.Primary)
                        .hoverEvent(HoverEvent.showText(
                            Component.text("Prepare untrack")
                        )
                        )
                        .clickEvent(
                            ClickEvent.suggestCommand("/t untrack " + username))
                )
                .append(
                    Component.text(",", Colours.Secondary)
                )
            ;
          }
          this.displayMessage(reply);
          return true;
        }
        case "untrack": {
          Component reply = Component.text("Click to untrack: ", Colours.Secondary);
          for (String username : friendTrackerManager.getTracking()) {
            reply = reply
                .append(
                    Component.text(username, Colours.Primary)
                        .hoverEvent(HoverEvent.showText(
                                Component.text("Click to untrack")
                            )
                        )
                        .clickEvent(
                            ClickEvent.runCommand("/t untrack " + username))
                )
                .append(
                    Component.text(",", Colours.Secondary)
                )
            ;
          }
          this.displayMessage(reply);
          return true;
        }
        default: {
          this.displayMessage(
              Component.text("Not a recognised option! /tracker (un)track [username]", Colours.Error)
          );
          return true;
        }
      }
    }

    switch (arguments[0]) {
      case "track": {
        Component reply = Component.text("Starting tracker: ", Colours.Secondary);
        for (int i = 1; i < arguments.length; i++) {
          friendTrackerManager.addTracking(arguments[i]);
          reply = reply.append(Component.text(arguments[i], Colours.Primary)
              .append(Component.text(",", Colours.Secondary)));
        }
        this.displayMessage(reply);
        friendTrackerManager.forceUpdate();
        return true;
      }
      case "untrack": {
        Component reply = Component.text("Stopped tracker: ", Colours.Secondary);
        for (int i = 1; i < arguments.length; i++) {
          friendTrackerManager.unTrack(arguments[i]);
          reply = reply.append(Component.text(arguments[i], Colours.Primary)
              .append(Component.text(",", Colours.Secondary)));
        }
        this.displayMessage(reply);
        return true;
      }
      default: {
        this.displayMessage(
            Component.text("Not a recognised option! /tracker (un)track [username]", Colours.Error)
        );
        return true;
      }
    }
  }
}
