package art.ameliah.laby.addons.cubepanion.v1_20_1.mixins;

import art.ameliah.laby.addons.cubepanion.core.accessors.CCCompoundTag;
import java.util.Map;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Interface.Remap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CompoundTag.class)
@Implements({@Interface(
    iface = CCCompoundTag.class,
    prefix = "compoundTag$",
    remap = Remap.NONE
)})
public abstract class MixingCompoundTag implements CCCompoundTag {

  @Final
  @Shadow
  private Map<String, Tag> tags;

  @Override
  public Optional<String> getString(String key) {
    try {
      return Optional.of(((Tag)this.tags.get(key)).getAsString());
    } catch (ClassCastException var3) {
    }

    return Optional.empty();
  }

}
