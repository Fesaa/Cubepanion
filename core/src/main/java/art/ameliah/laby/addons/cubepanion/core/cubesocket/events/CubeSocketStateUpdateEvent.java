package art.ameliah.laby.addons.cubepanion.core.cubesocket.events;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.session.CubeSocketState;
import net.labymod.api.event.Event;

public class CubeSocketStateUpdateEvent implements Event {

  private final CubeSocketState state;

  public CubeSocketStateUpdateEvent(CubeSocketState state) {
    this.state = state;
  }

  public CubeSocketState getState() {
    return state;
  }

}
