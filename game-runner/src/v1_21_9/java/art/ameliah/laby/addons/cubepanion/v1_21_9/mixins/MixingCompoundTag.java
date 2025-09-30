package art.ameliah.laby.addons.cubepanion.v1_21_9.mixins;

import art.ameliah.laby.addons.cubepanion.core.accessors.CCCompoundTag;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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

  @Shadow
  public abstract Optional<Tag> getOptional(String $$0);

  @Override
  public Optional<String> getString(String key) {
    return this.getOptional(key).flatMap(Tag::asString);
  }

}
