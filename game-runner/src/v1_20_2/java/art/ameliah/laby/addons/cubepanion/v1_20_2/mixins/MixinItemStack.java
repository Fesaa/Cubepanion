package art.ameliah.laby.addons.cubepanion.v1_20_2.mixins;

import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
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
  public abstract List<net.minecraft.network.chat.Component> getTooltipLines(@Nullable Player $$0, TooltipFlag $$1);

  @Override
  public List<String> getToolTips() {
    List<String> lines = new ArrayList<>();

    var toolTips = this.getTooltipLines(Minecraft.getInstance().player, TooltipFlag.NORMAL);
    for (var mcc : toolTips) {
      lines.add(mcc.getString());
    }

    return lines;
  }
}
