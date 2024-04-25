package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;

public class PacketSetProtocol extends Packet {

  private int protocolVersion;

  public PacketSetProtocol(int protocolVersion) {
    this.protocolVersion = protocolVersion;
  }

  public int getProtocolVersion() {
    return protocolVersion;
  }

  @Override
  public void read(PacketBuffer buf) {
    protocolVersion = buf.readInt();
  }

  @Override
  public void write(PacketBuffer buf) {
    buf.writeInt(protocolVersion);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
  }
}
