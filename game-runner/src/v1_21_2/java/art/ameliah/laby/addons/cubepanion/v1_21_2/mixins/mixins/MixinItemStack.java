package art.ameliah.laby.addons.cubepanion.v1_21_2.mixins.mixins;

import art.ameliah.laby.addons.cubepanion.core.accessors.CCCompoundTag;
import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import com.mojang.authlib.properties.Property;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item.TooltipContext;
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
  public abstract List<net.minecraft.network.chat.Component> getTooltipLines(
      TooltipContext $$0, @Nullable Player $$1, TooltipFlag $$2);

  @Shadow
  public abstract DataComponentMap getComponents();


  @Shadow
  public abstract DataComponentPatch getComponentsPatch();

  @Override
  public CCCompoundTag getCustomDataTag() {
    var data = this.getComponentsPatch().get(DataComponents.CUSTOM_DATA);
    if (data == null || data.isEmpty()) {
      return null;
    }

    var tag = data.get();

    return (CCCompoundTag) (Object) tag.copyTag();
  }

  @Override
  public List<String> getToolTips() {
    List<String> lines = new ArrayList<>();

    var toolTips = this.getTooltipLines(
        TooltipContext.of(Minecraft.getInstance().level), Minecraft.getInstance().player, TooltipFlag.NORMAL);

    for (var mcc : toolTips) {
      lines.add(mcc.getString());
    }

    return lines;
  }

  @Override
  public String texture() {
    var profile = this.getComponentsPatch().get(DataComponents.PROFILE);
    if (profile == null || profile.isEmpty()) {
      return null;
    }

    return profile.get().properties()
        .get("textures")
        .stream()
        .findFirst()
        .map(Property::value)
        .orElse(null);
  }
}
