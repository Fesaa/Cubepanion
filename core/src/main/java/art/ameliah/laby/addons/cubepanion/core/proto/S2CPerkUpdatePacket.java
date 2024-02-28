// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: packets.proto

// Protobuf Java Version: 3.25.2
package art.ameliah.laby.addons.cubepanion.core.proto;

/**
 * Protobuf type {@code art.ameliah.laby.addons.cubepanion.S2CPerkUpdatePacket}
 */
public final class S2CPerkUpdatePacket extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:art.ameliah.laby.addons.cubepanion.S2CPerkUpdatePacket)
    S2CPerkUpdatePacketOrBuilder {
private static final long serialVersionUID = 0L;
  // Use S2CPerkUpdatePacket.newBuilder() to construct.
  private S2CPerkUpdatePacket(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private S2CPerkUpdatePacket() {
    category_ = 0;
    perks_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
    uuid_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new S2CPerkUpdatePacket();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return art.ameliah.laby.addons.cubepanion.core.proto.Packets.internal_static_art_ameliah_laby_addons_cubepanion_S2CPerkUpdatePacket_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return art.ameliah.laby.addons.cubepanion.core.proto.Packets.internal_static_art_ameliah_laby_addons_cubepanion_S2CPerkUpdatePacket_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket.class, art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket.Builder.class);
  }

  public static final int CATEGORY_FIELD_NUMBER = 1;
  private int category_ = 0;
  /**
   * <code>.art.ameliah.laby.addons.cubepanion.PerkCategory category = 1;</code>
   * @return The enum numeric value on the wire for category.
   */
  @java.lang.Override public int getCategoryValue() {
    return category_;
  }
  /**
   * <code>.art.ameliah.laby.addons.cubepanion.PerkCategory category = 1;</code>
   * @return The category.
   */
  @java.lang.Override public art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory getCategory() {
    art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory result = art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory.forNumber(category_);
    return result == null ? art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory.UNRECOGNIZED : result;
  }

  public static final int PERKS_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private com.google.protobuf.LazyStringArrayList perks_ =
      com.google.protobuf.LazyStringArrayList.emptyList();
  /**
   * <code>repeated string perks = 2;</code>
   * @return A list containing the perks.
   */
  public com.google.protobuf.ProtocolStringList
      getPerksList() {
    return perks_;
  }
  /**
   * <code>repeated string perks = 2;</code>
   * @return The count of perks.
   */
  public int getPerksCount() {
    return perks_.size();
  }
  /**
   * <code>repeated string perks = 2;</code>
   * @param index The index of the element to return.
   * @return The perks at the given index.
   */
  public java.lang.String getPerks(int index) {
    return perks_.get(index);
  }
  /**
   * <code>repeated string perks = 2;</code>
   * @param index The index of the value to return.
   * @return The bytes of the perks at the given index.
   */
  public com.google.protobuf.ByteString
      getPerksBytes(int index) {
    return perks_.getByteString(index);
  }

  public static final int UUID_FIELD_NUMBER = 3;
  @SuppressWarnings("serial")
  private volatile java.lang.Object uuid_ = "";
  /**
   * <code>string uuid = 3;</code>
   * @return The uuid.
   */
  @java.lang.Override
  public java.lang.String getUuid() {
    java.lang.Object ref = uuid_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      uuid_ = s;
      return s;
    }
  }
  /**
   * <code>string uuid = 3;</code>
   * @return The bytes for uuid.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getUuidBytes() {
    java.lang.Object ref = uuid_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      uuid_ = b;
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
    if (category_ != art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory.PERSONAL.getNumber()) {
      output.writeEnum(1, category_);
    }
    for (int i = 0; i < perks_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, perks_.getRaw(i));
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(uuid_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, uuid_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (category_ != art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory.PERSONAL.getNumber()) {
      size += com.google.protobuf.CodedOutputStream
        .computeEnumSize(1, category_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < perks_.size(); i++) {
        dataSize += computeStringSizeNoTag(perks_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getPerksList().size();
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(uuid_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(3, uuid_);
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
    if (!(obj instanceof art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket)) {
      return super.equals(obj);
    }
    art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket other = (art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket) obj;

    if (category_ != other.category_) return false;
    if (!getPerksList()
        .equals(other.getPerksList())) return false;
    if (!getUuid()
        .equals(other.getUuid())) return false;
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
    hash = (37 * hash) + CATEGORY_FIELD_NUMBER;
    hash = (53 * hash) + category_;
    if (getPerksCount() > 0) {
      hash = (37 * hash) + PERKS_FIELD_NUMBER;
      hash = (53 * hash) + getPerksList().hashCode();
    }
    hash = (37 * hash) + UUID_FIELD_NUMBER;
    hash = (53 * hash) + getUuid().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket parseFrom(
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
  public static Builder newBuilder(art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket prototype) {
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
   * Protobuf type {@code art.ameliah.laby.addons.cubepanion.S2CPerkUpdatePacket}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:art.ameliah.laby.addons.cubepanion.S2CPerkUpdatePacket)
      art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacketOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return art.ameliah.laby.addons.cubepanion.core.proto.Packets.internal_static_art_ameliah_laby_addons_cubepanion_S2CPerkUpdatePacket_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return art.ameliah.laby.addons.cubepanion.core.proto.Packets.internal_static_art_ameliah_laby_addons_cubepanion_S2CPerkUpdatePacket_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket.class, art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket.Builder.class);
    }

    // Construct using art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket.newBuilder()
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
      category_ = 0;
      perks_ =
          com.google.protobuf.LazyStringArrayList.emptyList();
      uuid_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return art.ameliah.laby.addons.cubepanion.core.proto.Packets.internal_static_art_ameliah_laby_addons_cubepanion_S2CPerkUpdatePacket_descriptor;
    }

    @java.lang.Override
    public art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket getDefaultInstanceForType() {
      return art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket.getDefaultInstance();
    }

    @java.lang.Override
    public art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket build() {
      art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket buildPartial() {
      art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket result = new art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.category_ = category_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        perks_.makeImmutable();
        result.perks_ = perks_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.uuid_ = uuid_;
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
      if (other instanceof art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket) {
        return mergeFrom((art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket other) {
      if (other == art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket.getDefaultInstance()) return this;
      if (other.category_ != 0) {
        setCategoryValue(other.getCategoryValue());
      }
      if (!other.perks_.isEmpty()) {
        if (perks_.isEmpty()) {
          perks_ = other.perks_;
          bitField0_ |= 0x00000002;
        } else {
          ensurePerksIsMutable();
          perks_.addAll(other.perks_);
        }
        onChanged();
      }
      if (!other.getUuid().isEmpty()) {
        uuid_ = other.uuid_;
        bitField0_ |= 0x00000004;
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
            case 8: {
              category_ = input.readEnum();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 18: {
              java.lang.String s = input.readStringRequireUtf8();
              ensurePerksIsMutable();
              perks_.add(s);
              break;
            } // case 18
            case 26: {
              uuid_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000004;
              break;
            } // case 26
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

    private int category_ = 0;
    /**
     * <code>.art.ameliah.laby.addons.cubepanion.PerkCategory category = 1;</code>
     * @return The enum numeric value on the wire for category.
     */
    @java.lang.Override public int getCategoryValue() {
      return category_;
    }
    /**
     * <code>.art.ameliah.laby.addons.cubepanion.PerkCategory category = 1;</code>
     * @param value The enum numeric value on the wire for category to set.
     * @return This builder for chaining.
     */
    public Builder setCategoryValue(int value) {
      category_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>.art.ameliah.laby.addons.cubepanion.PerkCategory category = 1;</code>
     * @return The category.
     */
    @java.lang.Override
    public art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory getCategory() {
      art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory result = art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory.forNumber(category_);
      return result == null ? art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory.UNRECOGNIZED : result;
    }
    /**
     * <code>.art.ameliah.laby.addons.cubepanion.PerkCategory category = 1;</code>
     * @param value The category to set.
     * @return This builder for chaining.
     */
    public Builder setCategory(art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory value) {
      if (value == null) {
        throw new NullPointerException();
      }
      bitField0_ |= 0x00000001;
      category_ = value.getNumber();
      onChanged();
      return this;
    }
    /**
     * <code>.art.ameliah.laby.addons.cubepanion.PerkCategory category = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearCategory() {
      bitField0_ = (bitField0_ & ~0x00000001);
      category_ = 0;
      onChanged();
      return this;
    }

    private com.google.protobuf.LazyStringArrayList perks_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
    private void ensurePerksIsMutable() {
      if (!perks_.isModifiable()) {
        perks_ = new com.google.protobuf.LazyStringArrayList(perks_);
      }
      bitField0_ |= 0x00000002;
    }
    /**
     * <code>repeated string perks = 2;</code>
     * @return A list containing the perks.
     */
    public com.google.protobuf.ProtocolStringList
        getPerksList() {
      perks_.makeImmutable();
      return perks_;
    }
    /**
     * <code>repeated string perks = 2;</code>
     * @return The count of perks.
     */
    public int getPerksCount() {
      return perks_.size();
    }
    /**
     * <code>repeated string perks = 2;</code>
     * @param index The index of the element to return.
     * @return The perks at the given index.
     */
    public java.lang.String getPerks(int index) {
      return perks_.get(index);
    }
    /**
     * <code>repeated string perks = 2;</code>
     * @param index The index of the value to return.
     * @return The bytes of the perks at the given index.
     */
    public com.google.protobuf.ByteString
        getPerksBytes(int index) {
      return perks_.getByteString(index);
    }
    /**
     * <code>repeated string perks = 2;</code>
     * @param index The index to set the value at.
     * @param value The perks to set.
     * @return This builder for chaining.
     */
    public Builder setPerks(
        int index, java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      ensurePerksIsMutable();
      perks_.set(index, value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string perks = 2;</code>
     * @param value The perks to add.
     * @return This builder for chaining.
     */
    public Builder addPerks(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      ensurePerksIsMutable();
      perks_.add(value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string perks = 2;</code>
     * @param values The perks to add.
     * @return This builder for chaining.
     */
    public Builder addAllPerks(
        java.lang.Iterable<java.lang.String> values) {
      ensurePerksIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, perks_);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string perks = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearPerks() {
      perks_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
      bitField0_ = (bitField0_ & ~0x00000002);;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string perks = 2;</code>
     * @param value The bytes of the perks to add.
     * @return This builder for chaining.
     */
    public Builder addPerksBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      ensurePerksIsMutable();
      perks_.add(value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    private java.lang.Object uuid_ = "";
    /**
     * <code>string uuid = 3;</code>
     * @return The uuid.
     */
    public java.lang.String getUuid() {
      java.lang.Object ref = uuid_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        uuid_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string uuid = 3;</code>
     * @return The bytes for uuid.
     */
    public com.google.protobuf.ByteString
        getUuidBytes() {
      java.lang.Object ref = uuid_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        uuid_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string uuid = 3;</code>
     * @param value The uuid to set.
     * @return This builder for chaining.
     */
    public Builder setUuid(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      uuid_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>string uuid = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearUuid() {
      uuid_ = getDefaultInstance().getUuid();
      bitField0_ = (bitField0_ & ~0x00000004);
      onChanged();
      return this;
    }
    /**
     * <code>string uuid = 3;</code>
     * @param value The bytes for uuid to set.
     * @return This builder for chaining.
     */
    public Builder setUuidBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      uuid_ = value;
      bitField0_ |= 0x00000004;
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


    // @@protoc_insertion_point(builder_scope:art.ameliah.laby.addons.cubepanion.S2CPerkUpdatePacket)
  }

  // @@protoc_insertion_point(class_scope:art.ameliah.laby.addons.cubepanion.S2CPerkUpdatePacket)
  private static final art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket();
  }

  public static art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<S2CPerkUpdatePacket>
      PARSER = new com.google.protobuf.AbstractParser<S2CPerkUpdatePacket>() {
    @java.lang.Override
    public S2CPerkUpdatePacket parsePartialFrom(
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

  public static com.google.protobuf.Parser<S2CPerkUpdatePacket> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<S2CPerkUpdatePacket> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public art.ameliah.laby.addons.cubepanion.core.proto.S2CPerkUpdatePacket getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
