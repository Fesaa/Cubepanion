package art.ameliah.laby.addons.cubepanion.v1_20_4.mixins;

import art.ameliah.laby.addons.cubepanion.core.accessors.CCCompoundTag;
import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Interface.Remap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(net.minecraft.world.item.ItemStack.class)
@Implements({@Interface(
    iface = CCItemStack.class,
    prefix = "itemStack$",
    remap = Remap.NONE
)})
public abstract class MixinItemStack implements CCItemStack {

  @Shadow
  public abstract List<net.minecraft.network.chat.Component> getTooltipLines(@Nullable Player $$1, TooltipFlag $$2);

  @Shadow
  @Nullable
  public abstract CompoundTag getTag();

  @Override
  public CCCompoundTag getCustomDataTag() {
    return (CCCompoundTag) (Object) this.getTag();
  }

  @Override
  public List<String> getToolTips() {
    List<String> lines = new ArrayList<>();

    var toolTips = this.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.NORMAL);

    for (var mcc : toolTips) {
      lines.add(mcc.getString());
    }

    return lines;
  }

  @Override
  public String texture() {
    CompoundTag tag = getTag();
    if (tag == null) {
      return null;
    }

    if (!tag.contains("SkullOwner", 10)) {
      return null;
    }

    CompoundTag skullOwner = tag.getCompound("SkullOwner");
    if (!skullOwner.contains("Properties", 10)) {
      return null;
    }

    CompoundTag properties = skullOwner.getCompound("Properties");
    if (!properties.contains("textures", 9)) {
      return null;
    }

    ListTag textures = properties.getList("textures", 10);
    if (textures.isEmpty()) {
      return null;
    }

    CompoundTag texture = textures.getCompound(0);
    if (!texture.contains("Value", 8)) {
      return null;
    }
    return texture.getString("Value");
  }
}
