package org.cubepanion.core.utils;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;

public record OnlineFriendLocation(String username, String game, String map) {

  public OnlineFriendLocation(String username, String game, String map) {
    this.username = username;
    this.game = game;
    this.map = map != null ? map : "";
  }

  public String getLocationString() {
    if (this.map.isEmpty()) {
      return this.game;
    }
    return this.game + " (" + this.map + ")";
  }

  public boolean hasMoved(OnlineFriendLocation newLocation) {
    return
        !(this.username.equals(newLocation.username())
            && this.game.equals(newLocation.game())
            && this.map.equals(newLocation.map()));
  }

  public void broadcastMove(OnlineFriendLocation newLocation) {
    if (!this.hasMoved(newLocation)) {
      return;
    }

    Laby.labyAPI().minecraft().chatExecutor().displayClientMessage(
        Component.text(this.username, Colours.Primary)
            .append(Component.text(" moved from ", Colours.Secondary))
            .append(Component.text(this.getLocationString(), Colours.Primary))
            .append(Component.text(" to ", Colours.Secondary))
            .append(Component.text(newLocation.getLocationString(), Colours.Primary))
    );
  }
}
