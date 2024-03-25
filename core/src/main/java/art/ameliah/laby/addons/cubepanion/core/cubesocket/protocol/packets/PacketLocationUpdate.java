package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import art.ameliah.laby.addons.cubepanion.core.events.GameUpdateEvent;

public class PacketLocationUpdate extends Packet {

  private String origin;

  private String destination;

  private boolean preLobby;

  public PacketLocationUpdate() {
  }

  public PacketLocationUpdate(String origin, String destination, boolean preLobby) {
    this.origin = origin;
    this.destination = destination;
    this.preLobby = preLobby;
  }

  public PacketLocationUpdate(GameUpdateEvent e) {
    String serverID = Cubepanion.get().getManager().getServerID();
    String lastServerID = Cubepanion.get().getManager().getLastServerID();

    String map = Cubepanion.get().getManager().getMapName().toLowerCase().replace(" ", "_");
    String lastMap = Cubepanion.get().getManager().getLastMapName().toLowerCase().replace(" ", "_");

    this.origin= e.getOrigin().getString().toLowerCase().replace(" ", "_") + "-" + lastMap + "-" + lastServerID;
    this.destination = e.getDestination().getString().toLowerCase().replace(" ", "_") + "-" + map + "-" + serverID;
    this.preLobby = e.isPreLobby();
  }

  @Override
  public void read(PacketBuffer buf) {
    this.origin = buf.readString();
    this.destination = buf.readString();
    this.preLobby = buf.readBoolean();
  }

  @Override
  public void write(PacketBuffer buf) {
    buf.writeString(this.origin);
    buf.writeString(this.destination);
    buf.writeBoolean(this.preLobby);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
  }

  public String getOrigin() {
    return this.origin;
  }

  public String getDestination() {
    return this.destination;
  }

  public boolean isPreLobby() {
    return this.preLobby;
  }

}
