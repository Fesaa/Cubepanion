package art.ameliah.laby.addons.cubepanion.core.cubesocket.session;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.CubeSocket;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketUtils;
import art.ameliah.laby.addons.cubepanion.core.events.GameUpdateEvent;
import net.labymod.api.event.Subscribe;

public class CubeSocketGameTracker {

  private final CubeSocket socket;

  public CubeSocketGameTracker(CubeSocket socket) {
    this.socket = socket;
  }

  @Subscribe
  public void onGameUpdate(GameUpdateEvent e) {
    if (socket.getState() != CubeSocketState.CONNECTED) {
      return;
    }
    socket.sendPacket(PacketUtils.UpdateLocationPacket(e));
  }


}
