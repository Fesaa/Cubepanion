package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.GameUpdateEvent;
import art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket;
import art.ameliah.laby.addons.cubepanion.core.proto.C2SHelloPingPacket;
import art.ameliah.laby.addons.cubepanion.core.proto.C2SPacket;
import art.ameliah.laby.addons.cubepanion.core.proto.C2SPerkUpdatePacket;
import art.ameliah.laby.addons.cubepanion.core.proto.C2SUpdateLocationPacket;
import art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;

public class PacketUtils {

  public static C2SPacket HelloPingPacket(long time) {
    return C2SPacket
        .newBuilder()
        .setHelloPing(
            C2SHelloPingPacket
                .newBuilder()
                .setTime(time)
                .build())
        .build();
  }

  public static C2SPacket DisconnectPacket(String reason) {
    return C2SPacket
        .newBuilder()
        .setDisconnect(
            C2SDisconnectPacket
                .newBuilder()
                .setReason(reason)
                .build())
        .build();
  }

  public static C2SPacket UpdateLocationPacket(GameUpdateEvent e) {
    return UpdateLocationPacket(e.getOrigin(), e.getDestination(), e.isPreLobby());
  }

  public static C2SPacket UpdateLocationPacket(CubeGame origin, CubeGame destination, boolean preLobby) {
    String serverID = Cubepanion.get().getManager().getServerID();
    String lastServerID = Cubepanion.get().getManager().getLastServerID();

    String map = Cubepanion.get().getManager().getMapName().toLowerCase().replace(" ", "_");
    String lastMap = Cubepanion.get().getManager().getLastMapName().toLowerCase().replace(" ", "_");

    String originString = origin.getString().toLowerCase().replace(" ", "_") + "-" + lastMap + "-" + lastServerID;
    String destinationString = destination.getString().toLowerCase().replace(" ", "_") + "-" + map + "-" + serverID;

    return C2SPacket
        .newBuilder()
        .setUpdateLocation(
            C2SUpdateLocationPacket
                .newBuilder()
                .setOrigin(originString)
                .setDestination(destinationString)
                .setPreLobby(preLobby)
                .build())
        .build();
  }

  public static C2SPacket PerkUpdatePacket(PerkCategory category, Iterable<String> perkTags) {
    return C2SPacket
        .newBuilder()
        .setUpdatePerk(
            C2SPerkUpdatePacket
                .newBuilder()
                .addAllPerks(perkTags)
                .setCategory(category)
                .build())
        .build();
  }

  public static C2SPacket wrap(C2SDisconnectPacket packet) {
    return C2SPacket
        .newBuilder()
        .setDisconnect(packet)
        .build();
  }

  public static C2SPacket wrap(C2SHelloPingPacket packet) {
    return C2SPacket
        .newBuilder()
        .setHelloPing(packet)
        .build();
  }

  public static C2SPacket wrap(C2SUpdateLocationPacket packet) {
    return C2SPacket
        .newBuilder()
        .setUpdateLocation(packet)
        .build();
  }

  public static C2SPacket wrap(C2SPerkUpdatePacket packet) {
    return C2SPacket
        .newBuilder()
        .setUpdatePerk(packet)
        .build();
  }

}
