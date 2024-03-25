package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import java.util.UUID;

public class PacketLogin extends Packet {

  private UUID uuid;

  public PacketLogin() {
  }

  public PacketLogin(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public void read(PacketBuffer buf) {

  }

  @Override
  public void write(PacketBuffer buf) {
    buf.writeUUID(this.uuid);
  }

  @Override
  public void handle(PacketHandler packetHandler) {

  }
}
