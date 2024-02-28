// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: packets.proto

// Protobuf Java Version: 3.25.2
package art.ameliah.laby.addons.cubepanion.core.proto;

/**
 * Protobuf type {@code art.ameliah.laby.addons.cubepanion.C2SDisconnectPacket}
 */
public final class C2SDisconnectPacket extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:art.ameliah.laby.addons.cubepanion.C2SDisconnectPacket)
    C2SDisconnectPacketOrBuilder {
private static final long serialVersionUID = 0L;
  // Use C2SDisconnectPacket.newBuilder() to construct.
  private C2SDisconnectPacket(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private C2SDisconnectPacket() {
    reason_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new C2SDisconnectPacket();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return art.ameliah.laby.addons.cubepanion.core.proto.Packets.internal_static_art_ameliah_laby_addons_cubepanion_C2SDisconnectPacket_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return art.ameliah.laby.addons.cubepanion.core.proto.Packets.internal_static_art_ameliah_laby_addons_cubepanion_C2SDisconnectPacket_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket.class, art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket.Builder.class);
  }

  public static final int REASON_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile java.lang.Object reason_ = "";
  /**
   * <code>string reason = 1;</code>
   * @return The reason.
   */
  @java.lang.Override
  public java.lang.String getReason() {
    java.lang.Object ref = reason_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      reason_ = s;
      return s;
    }
  }
  /**
   * <code>string reason = 1;</code>
   * @return The bytes for reason.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getReasonBytes() {
    java.lang.Object ref = reason_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      reason_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(reason_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, reason_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(reason_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, reason_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket)) {
      return super.equals(obj);
    }
    art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket other = (art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket) obj;

    if (!getReason()
        .equals(other.getReason())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + REASON_FIELD_NUMBER;
    hash = (53 * hash) + getReason().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code art.ameliah.laby.addons.cubepanion.C2SDisconnectPacket}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:art.ameliah.laby.addons.cubepanion.C2SDisconnectPacket)
      art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacketOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return art.ameliah.laby.addons.cubepanion.core.proto.Packets.internal_static_art_ameliah_laby_addons_cubepanion_C2SDisconnectPacket_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return art.ameliah.laby.addons.cubepanion.core.proto.Packets.internal_static_art_ameliah_laby_addons_cubepanion_C2SDisconnectPacket_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket.class, art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket.Builder.class);
    }

    // Construct using art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      reason_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return art.ameliah.laby.addons.cubepanion.core.proto.Packets.internal_static_art_ameliah_laby_addons_cubepanion_C2SDisconnectPacket_descriptor;
    }

    @java.lang.Override
    public art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket getDefaultInstanceForType() {
      return art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket.getDefaultInstance();
    }

    @java.lang.Override
    public art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket build() {
      art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket buildPartial() {
      art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket result = new art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.reason_ = reason_;
      }
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket) {
        return mergeFrom((art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket other) {
      if (other == art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket.getDefaultInstance()) return this;
      if (!other.getReason().isEmpty()) {
        reason_ = other.reason_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              reason_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private java.lang.Object reason_ = "";
    /**
     * <code>string reason = 1;</code>
     * @return The reason.
     */
    public java.lang.String getReason() {
      java.lang.Object ref = reason_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        reason_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string reason = 1;</code>
     * @return The bytes for reason.
     */
    public com.google.protobuf.ByteString
        getReasonBytes() {
      java.lang.Object ref = reason_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        reason_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string reason = 1;</code>
     * @param value The reason to set.
     * @return This builder for chaining.
     */
    public Builder setReason(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      reason_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>string reason = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearReason() {
      reason_ = getDefaultInstance().getReason();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <code>string reason = 1;</code>
     * @param value The bytes for reason to set.
     * @return This builder for chaining.
     */
    public Builder setReasonBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      reason_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:art.ameliah.laby.addons.cubepanion.C2SDisconnectPacket)
  }

  // @@protoc_insertion_point(class_scope:art.ameliah.laby.addons.cubepanion.C2SDisconnectPacket)
  private static final art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket();
  }

  public static art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<C2SDisconnectPacket>
      PARSER = new com.google.protobuf.AbstractParser<C2SDisconnectPacket>() {
    @java.lang.Override
    public C2SDisconnectPacket parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<C2SDisconnectPacket> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<C2SDisconnectPacket> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public art.ameliah.laby.addons.cubepanion.core.proto.C2SDisconnectPacket getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
