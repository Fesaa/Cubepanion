package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;

public class PacketGameStatUpdate extends Packet {

  private CubeGame game;
  private int playerCount;
  private long timestamp;

  public PacketGameStatUpdate(CubeGame game, int playerCount) {
    this.game = game;
    this.playerCount = playerCount;
    this.timestamp = System.currentTimeMillis();
  }

  @Override
  public void read(PacketBuffer buf) {
    this.game = CubeGame.stringToGame(buf.readString());
    this.playerCount = buf.readInt();
    this.timestamp = buf.readLong();
  }

  @Override
  public void write(PacketBuffer buf) {
    buf.writeString(this.game.getString());
    buf.writeInt(this.playerCount);
    buf.writeLong(this.timestamp);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
  }
}
