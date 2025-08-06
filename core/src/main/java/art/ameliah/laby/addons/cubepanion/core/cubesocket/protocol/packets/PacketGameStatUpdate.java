package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.external.Game;

public class PacketGameStatUpdate extends Packet {

  private Game game;
  private int playerCount;
  private long timestamp;

  public PacketGameStatUpdate(Game game, int playerCount) {
    this.game = game;
    this.playerCount = playerCount;
    this.timestamp = System.currentTimeMillis();
  }

  public Game getGame() {
    return game;
  }

  @Override
  public void read(PacketBuffer buf) {
    String s= buf.readString();
    Game game = CubepanionAPI.I().tryGame(s);
    if (game == null) {
      throw new IllegalArgumentException("Unknown name: "+ s);
    }

    this.game = game;
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
