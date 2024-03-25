package art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.packets;

import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.Packet;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketBuffer;
import art.ameliah.laby.addons.cubepanion.core.cubesocket.protocol.PacketHandler;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent.PerkCategory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;

public class PacketPerkUpdate extends Packet {

  private static final Gson gson = new Gson();
  private static final Logging LOGGER = Logging.create(PacketPerkUpdate.class);

  private PerkCategory perkCategory;

  private UUID sender;

  private byte[] perks;

  public PacketPerkUpdate() {
  }

  public PacketPerkUpdate(PerkCategory perkCategory, byte[] perks) {
    this.perkCategory = perkCategory;
    this.perks = perks;
  }

  public PacketPerkUpdate(PerkCategory perkCategory, UUID sender, Iterable<String> perks) {
    this.perkCategory = perkCategory;
    this.sender = sender;

    JsonArray jsonObject = new JsonArray();
    for (String perk : perks) {
      jsonObject.add(perk);
    }

    this.perks = toBytes(jsonObject.toString());
  }

  @Override
  public void read(PacketBuffer buf) {
    this.perkCategory = PerkCategory.fromCubeTapItemVariant(buf.readString());
    if (this.perkCategory == null) {
      throw new RuntimeException("Invalid perk category");
    }

    this.sender = buf.readUUID();

    byte[] data = new byte[buf.readInt()];
    buf.readBytes(data);
    this.perks = data;
  }

  @Override
  public void write(PacketBuffer buf) {
    buf.writeString(this.perkCategory.getCubeTapItemVariant());
    buf.writeUUID(this.sender);
    buf.writeInt(this.perks.length);
    buf.writeBytes(this.perks);
  }

  @Override
  public void handle(PacketHandler packetHandler) {
    packetHandler.handle(this);
  }

  public @NotNull PerkCategory getPerkCategory() {
    return perkCategory;
  }

  public UUID getSender() {
    return sender;
  }

  public String[] getPerks() {
    String[] array;
    try {
      array = gson.fromJson(this.getJson(), String[].class);
    } catch (JsonSyntaxException exception) {
      LOGGER.error("Failed to parse perks", exception);
      return new String[0];
    }

    return array;
  }

  private String getJson() {
    try {
      StringBuilder outStr = new StringBuilder();
      if (this.perks != null && this.perks.length != 0) {
        if (this.isCompressed(this.perks)) {
          GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(this.perks));
          BufferedReader bufferedReader = new BufferedReader(
              new InputStreamReader(gis, StandardCharsets.UTF_8));

          String line;
          while ((line = bufferedReader.readLine()) != null) {
            outStr.append(line);
          }
        } else {
          outStr.append(Arrays.toString(this.perks));
        }

        return outStr.toString();
      } else {
        return "";
      }
    } catch (IOException var5) {
      LOGGER.error("Failed to decompress perks", var5);
      return "";
    }
  }

  private byte[] toBytes(String in) {
    byte[] str = in.getBytes(StandardCharsets.UTF_8);

    try {
      if (str.length == 0) {
        return new byte[0];
      } else {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(byteArrayOutputStream);
        gzip.write(str);
        gzip.flush();
        gzip.close();
        return byteArrayOutputStream.toByteArray();
      }
    } catch (IOException exception) {
      LOGGER.error("Failed to compress perks", exception);
      return new byte[0];
    }
  }

  private boolean isCompressed(byte[] compressed) {
    return compressed[0] == 31 && compressed[1] == -117;
  }
}
