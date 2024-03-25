package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import net.labymod.api.util.time.TimeUtil;

public class PacketHelloPing extends Packet {

  private long timestamp;

  public PacketHelloPing() {
    this.timestamp = TimeUtil.getCurrentTimeMillis();
  }

  public PacketHelloPing(long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public void read(PacketBuffer buf) {
    this.timestamp = buf.readLong();
  }

  @Override
  public void write(PacketBuffer buf) {
    buf.writeLong(this.timestamp);
  }

  @Override
  public void handle(PacketHandler packetHandler) {

  }

  public long getTimestamp() {
    return this.timestamp;
  }
}
