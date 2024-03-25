package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol;

public abstract class Packet {

  public abstract void read(PacketBuffer buf);

  public abstract void write(PacketBuffer buf);

  public abstract void handle(PacketHandler packetHandler);

}
