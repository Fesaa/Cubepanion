// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: packets.proto

// Protobuf Java Version: 3.25.3
package art.ameliah.laby.addons.cubepanion.core.proto;

public final class Packets {
  private Packets() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_art_ameliah_laby_addons_cubepanion_S2CPacket_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_art_ameliah_laby_addons_cubepanion_S2CPacket_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_art_ameliah_laby_addons_cubepanion_S2CPingPacket_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_art_ameliah_laby_addons_cubepanion_S2CPingPacket_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_art_ameliah_laby_addons_cubepanion_S2CPerkUpdatePacket_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_art_ameliah_laby_addons_cubepanion_S2CPerkUpdatePacket_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_art_ameliah_laby_addons_cubepanion_C2SPacket_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_art_ameliah_laby_addons_cubepanion_C2SPacket_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_art_ameliah_laby_addons_cubepanion_C2SPingPacket_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_art_ameliah_laby_addons_cubepanion_C2SPingPacket_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_art_ameliah_laby_addons_cubepanion_C2SDisconnectPacket_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_art_ameliah_laby_addons_cubepanion_C2SDisconnectPacket_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_art_ameliah_laby_addons_cubepanion_C2SUpdateLocationPacket_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_art_ameliah_laby_addons_cubepanion_C2SUpdateLocationPacket_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_art_ameliah_laby_addons_cubepanion_C2SPerkUpdatePacket_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_art_ameliah_laby_addons_cubepanion_C2SPerkUpdatePacket_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rpackets.proto\022\"art.ameliah.laby.addons" +
      ".cubepanion\"\247\001\n\tS2CPacket\022M\n\nupdatePerk\030" +
      "\001 \001(\01327.art.ameliah.laby.addons.cubepani" +
      "on.S2CPerkUpdatePacketH\000\022A\n\004ping\030\003 \001(\01321" +
      ".art.ameliah.laby.addons.cubepanion.S2CP" +
      "ingPacketH\000B\010\n\006packet\"\017\n\rS2CPingPacket\"v" +
      "\n\023S2CPerkUpdatePacket\022B\n\010category\030\001 \001(\0162" +
      "0.art.ameliah.laby.addons.cubepanion.Per" +
      "kCategory\022\r\n\005perks\030\002 \003(\t\022\014\n\004uuid\030\003 \001(\t\"\315" +
      "\002\n\tC2SPacket\022U\n\016updateLocation\030\001 \001(\0132;.a" +
      "rt.ameliah.laby.addons.cubepanion.C2SUpd" +
      "ateLocationPacketH\000\022M\n\nupdatePerk\030\002 \001(\0132" +
      "7.art.ameliah.laby.addons.cubepanion.C2S" +
      "PerkUpdatePacketH\000\022M\n\ndisconnect\030\004 \001(\01327" +
      ".art.ameliah.laby.addons.cubepanion.C2SD" +
      "isconnectPacketH\000\022A\n\004ping\030\005 \001(\01321.art.am" +
      "eliah.laby.addons.cubepanion.C2SPingPack" +
      "etH\000B\010\n\006packet\"\017\n\rC2SPingPacket\"%\n\023C2SDi" +
      "sconnectPacket\022\016\n\006reason\030\001 \001(\t\"P\n\027C2SUpd" +
      "ateLocationPacket\022\016\n\006origin\030\001 \001(\t\022\023\n\013des" +
      "tination\030\002 \001(\t\022\020\n\010preLobby\030\003 \001(\010\"h\n\023C2SP" +
      "erkUpdatePacket\022B\n\010category\030\001 \001(\01620.art." +
      "ameliah.laby.addons.cubepanion.PerkCateg" +
      "ory\022\r\n\005perks\030\002 \003(\t*0\n\014PerkCategory\022\014\n\010PE" +
      "RSONAL\020\000\022\010\n\004TEAM\020\001\022\010\n\004GAME\020\002BI\n-art.amel" +
      "iah.laby.addons.cubepanion.core.protoB\007P" +
      "acketsP\001Z\rproto/packetsb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_art_ameliah_laby_addons_cubepanion_S2CPacket_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_art_ameliah_laby_addons_cubepanion_S2CPacket_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_art_ameliah_laby_addons_cubepanion_S2CPacket_descriptor,
        new java.lang.String[] { "UpdatePerk", "Ping", "Packet", });
    internal_static_art_ameliah_laby_addons_cubepanion_S2CPingPacket_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_art_ameliah_laby_addons_cubepanion_S2CPingPacket_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_art_ameliah_laby_addons_cubepanion_S2CPingPacket_descriptor,
        new java.lang.String[] { });
    internal_static_art_ameliah_laby_addons_cubepanion_S2CPerkUpdatePacket_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_art_ameliah_laby_addons_cubepanion_S2CPerkUpdatePacket_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_art_ameliah_laby_addons_cubepanion_S2CPerkUpdatePacket_descriptor,
        new java.lang.String[] { "Category", "Perks", "Uuid", });
    internal_static_art_ameliah_laby_addons_cubepanion_C2SPacket_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_art_ameliah_laby_addons_cubepanion_C2SPacket_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_art_ameliah_laby_addons_cubepanion_C2SPacket_descriptor,
        new java.lang.String[] { "UpdateLocation", "UpdatePerk", "Disconnect", "Ping", "Packet", });
    internal_static_art_ameliah_laby_addons_cubepanion_C2SPingPacket_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_art_ameliah_laby_addons_cubepanion_C2SPingPacket_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_art_ameliah_laby_addons_cubepanion_C2SPingPacket_descriptor,
        new java.lang.String[] { });
    internal_static_art_ameliah_laby_addons_cubepanion_C2SDisconnectPacket_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_art_ameliah_laby_addons_cubepanion_C2SDisconnectPacket_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_art_ameliah_laby_addons_cubepanion_C2SDisconnectPacket_descriptor,
        new java.lang.String[] { "Reason", });
    internal_static_art_ameliah_laby_addons_cubepanion_C2SUpdateLocationPacket_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_art_ameliah_laby_addons_cubepanion_C2SUpdateLocationPacket_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_art_ameliah_laby_addons_cubepanion_C2SUpdateLocationPacket_descriptor,
        new java.lang.String[] { "Origin", "Destination", "PreLobby", });
    internal_static_art_ameliah_laby_addons_cubepanion_C2SPerkUpdatePacket_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_art_ameliah_laby_addons_cubepanion_C2SPerkUpdatePacket_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_art_ameliah_laby_addons_cubepanion_C2SPerkUpdatePacket_descriptor,
        new java.lang.String[] { "Category", "Perks", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
