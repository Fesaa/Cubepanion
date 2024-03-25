package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;

public class PacketDisconnect extends Packet {

  private String reason;

  public PacketDisconnect() {
    this.reason = "Unknown";
  }

  public PacketDisconnect(String reason) {
    this.reason = reason;
  }

  @Override
  public void read(PacketBuffer buf) {
    this.reason = buf.readString();
  }

  @Override
  public void write(PacketBuffer buf) {
    buf.writeString(this.reason);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
  }
}
