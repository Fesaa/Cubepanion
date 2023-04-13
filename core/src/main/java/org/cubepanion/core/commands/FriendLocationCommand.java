package org.cubepanion.core.commands;

import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.component.Component;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.managers.CCUManager;
import org.cubepanion.core.managers.submanagers.FriendTrackerManager;
import org.cubepanion.core.utils.eggwarsmaps.OnlineFriendLocation;

public class FriendLocationCommand extends Command {

  private final CCUManager ccuManager;
  private final FriendTrackerManager manager;

  public FriendLocationCommand(Cubepanion addon) {
    super("friendlocations");

    this.ccuManager = addon.getManager();
    this.manager = this.ccuManager.getFriendTrackerManager();

    this.messagePrefix = addon.prefix();
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!this.ccuManager.onCubeCraft()) {
      return false;
    }
    Component component = Component.empty();
    OnlineFriendLocation[] onlineFriendLocations = this.manager
        .getOnlineFriendLocations().toArray(new OnlineFriendLocation[0]);

    for (int i = 0; i < onlineFriendLocations.length; i++) {
      if (i != 0) {
        component = component.append(Component.text(", "));
      }
      component = component.append(this.locationToComponent(onlineFriendLocations[i]));
    }
    this.displayMessage(component);
    return true;
  }


  private Component locationToComponent(OnlineFriendLocation location) {
    return Component.text("{username: " + location.getUsername() + ", game: " + location.getGame() + ", map: " + location.getMap() + "}");
  }
}
