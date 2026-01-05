package art.ameliah.laby.addons.cubepanion.v1_21_2;

import art.ameliah.laby.addons.cubepanion.core.versionlinkers.CodecLink;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import java.util.Optional;
import javax.inject.Singleton;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;

@Singleton
@Implements(CodecLink.class)
public class VersionedCodecLink extends CodecLink {

  @Override
  public Optional<JsonElement> encode(ItemStack itemStack) {
    DataResult<JsonElement> encoded = net.minecraft.world.item.ItemStack.CODEC.encodeStart(
        JsonOps.INSTANCE, (net.minecraft.world.item.ItemStack) (Object) itemStack);
    return encoded.result();
  }

  @Override
  public Optional<ItemStack> decode(JsonElement jsonElement) {
    Optional<net.minecraft.world.item.ItemStack> decoded = net.minecraft.world.item.ItemStack.CODEC
        .parse(JsonOps.INSTANCE, jsonElement).result();
    return decoded.map(itemStack -> (ItemStack) (Object) itemStack);

  }
}
