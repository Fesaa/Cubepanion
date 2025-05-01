package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.weave.APIGame;
import art.ameliah.laby.addons.cubepanion.core.weave.GamesAPI;

public class PacketGameStatUpdate extends Packet {

  private APIGame game;
  private int playerCount;
  private long timestamp;

  public PacketGameStatUpdate(APIGame game, int playerCount) {
    this.game = game;
    this.playerCount = playerCount;
    this.timestamp = System.currentTimeMillis();
  }

  @Override
  public void read(PacketBuffer buf) {
    this.game = GamesAPI.I().getGame(buf.readString());
    this.playerCount = buf.readInt();
    this.timestamp = buf.readLong();
  }

  @Override
  public void write(PacketBuffer buf) {
    buf.writeString(this.game.displayName());
    buf.writeInt(this.playerCount);
    buf.writeLong(this.timestamp);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
  }
}
