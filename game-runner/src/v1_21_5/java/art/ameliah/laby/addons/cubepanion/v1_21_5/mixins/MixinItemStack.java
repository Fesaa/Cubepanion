package art.ameliah.laby.addons.cubepanion.v1_21_5.mixins;

import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import net.labymod.api.client.component.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Interface.Remap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.ArrayList;
import java.util.List;

@Mixin(net.minecraft.world.item.ItemStack.class)
@Implements({@Interface(
    iface = art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack.class,
    prefix = "itemStack$",
    remap = Remap.NONE
)})
public abstract class MixinItemStack implements CCItemStack {

  @Shadow
  public abstract List<net.minecraft.network.chat.Component> getTooltipLines(
      Item.TooltipContext $$0, @Nullable Player $$1, TooltipFlag $$2);

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
}
