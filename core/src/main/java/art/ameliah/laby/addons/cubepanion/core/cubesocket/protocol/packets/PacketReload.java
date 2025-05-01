package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;

public class PacketReload extends Packet {

  public PacketReload() {}

  @Override
  public void read(PacketBuffer buf) {

  }

  @Override
  public void write(PacketBuffer buf) {

  }

  @Override
  public void handle(PacketHandler packetHandler) {
    packetHandler.handle(this);
  }
}
