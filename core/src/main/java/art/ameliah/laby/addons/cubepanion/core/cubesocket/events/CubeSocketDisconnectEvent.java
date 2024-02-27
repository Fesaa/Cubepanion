package art.ameliah.laby.addons.cubepanion.core.cubesocket.events;

import net.labymod.api.event.Event;

public class CubeSocketDisconnectEvent implements Event {

  private final String reason;

  public CubeSocketDisconnectEvent(String reason) {
    this.reason = reason;
  }

  public String getReason() {
    return reason;
  }

}
