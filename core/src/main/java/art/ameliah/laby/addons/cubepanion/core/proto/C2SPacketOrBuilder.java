// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: packets.proto

// Protobuf Java Version: 3.25.2
package art.ameliah.laby.addons.cubepanion.core.proto;

public interface C2SPacketOrBuilder extends
    // @@protoc_insertion_point(interface_extends:art.ameliah.laby.addons.cubepanion.C2SPacket)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SUpdateLocationPacket updateLocation = 1;</code>
   * @return Whether the updateLocation field is set.
   */
  boolean hasUpdateLocation();
  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SUpdateLocationPacket updateLocation = 1;</code>
   * @return The updateLocation.
   */
  art.ameliah.laby.addons.cubepanion.core.proto.C2SUpdateLocationPacket getUpdateLocation();
  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SUpdateLocationPacket updateLocation = 1;</code>
   */
  art.ameliah.laby.addons.cubepanion.core.proto.C2SUpdateLocationPacketOrBuilder getUpdateLocationOrBuilder();

  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SPerkUpdatePacket updatePerk = 2;</code>
   * @return Whether the updatePerk field is set.
   */
  boolean hasUpdatePerk();
  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SPerkUpdatePacket updatePerk = 2;</code>
   * @return The updatePerk.
   */
  art.ameliah.laby.addons.cubepanion.core.proto.C2SPerkUpdatePacket getUpdatePerk();
  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SPerkUpdatePacket updatePerk = 2;</code>
   */
  art.ameliah.laby.addons.cubepanion.core.proto.C2SPerkUpdatePacketOrBuilder getUpdatePerkOrBuilder();

  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SHelloPingPacket helloPing = 3;</code>
   * @return Whether the helloPing field is set.
   */
  boolean hasHelloPing();
  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SHelloPingPacket helloPing = 3;</code>
   * @return The helloPing.
   */
  art.ameliah.laby.addons.cubepanion.core.proto.C2SHelloPingPacket getHelloPing();
  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SHelloPingPacket helloPing = 3;</code>
   */
  art.ameliah.laby.addons.cubepanion.core.proto.C2SHelloPingPacketOrBuilder getHelloPingOrBuilder();

  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SDisconnectPacket disconnect = 4;</code>
   * @return Whether the disconnect field is set.
   */
  boolean hasDisconnect();
  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SDisconnectPacket disconnect = 4;</code>
   * @return The disconnect.
   */
  art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket getDisconnect();
  /**
   * <code>.art.ameliah.laby.addons.cubepanion.C2SDisconnectPacket disconnect = 4;</code>
   */
  art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacketOrBuilder getDisconnectOrBuilder();

  art.ameliah.laby.addons.cubepanion.core.proto.C2SPacket.PacketCase getPacketCase();
}