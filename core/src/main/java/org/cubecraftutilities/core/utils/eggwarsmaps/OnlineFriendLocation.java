package org.cubecraftutilities.core.utils.eggwarsmaps;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import org.cubecraftutilities.core.utils.Colours;

public class OnlineFriendLocation {

  private final String username;
  private final String game;
  private final String map;

  public OnlineFriendLocation(String username, String game, String map) {
    this.username = username;
    this.game = game;
    this.map = map != null ? map : "";
  }

  public String getGame() {
    return game;
  }

  public String getMap() {
    return map;
  }

  public String getUsername() {
    return username;
  }

  public String getLocationString() {
    if (this.map.equals("")) {
      return this.game;
    }
    return this.game + " (" + this.map + ")";
  }

  public boolean hasMoved(OnlineFriendLocation newLocation) {
    return
        !(this.username.equals(newLocation.getUsername())
        && this.game.equals(newLocation.getGame())
        && this.map.equals(newLocation.getMap()));
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
