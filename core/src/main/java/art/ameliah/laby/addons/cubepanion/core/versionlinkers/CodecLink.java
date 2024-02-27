package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import com.google.gson.JsonElement;
import java.util.Optional;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class CodecLink {

  public abstract Optional<JsonElement> encode(ItemStack itemStack);

  public abstract Optional<ItemStack> decode(JsonElement jsonElement);

}
