package art.ameliah.laby.addons.cubepanion.v1_20_2;

import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.labymod.v1_20_2.client.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import org.jetbrains.annotations.NotNull;

@Singleton
@Implements(FunctionLink.class)
public class VersionedFunctionLink extends FunctionLink {

  @Override
  public void setCoolDown(@NotNull net.labymod.api.client.world.item.ItemStack itemStack,
      int duration) {
    if (Minecraft.getInstance().player == null) {
      return;
    }
    ItemStack mcItem = ItemUtil.getMinecraftItemStack(itemStack);
    if (mcItem == null) {
      return;
    }
    Minecraft.getInstance().player.getCooldowns().addCooldown(mcItem.getItem(), duration);
  }
}
