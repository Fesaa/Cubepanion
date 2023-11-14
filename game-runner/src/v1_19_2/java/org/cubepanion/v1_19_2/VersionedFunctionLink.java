package org.cubepanion.v1_19_2;

import net.labymod.api.models.Implements;
import net.labymod.v1_19_2.client.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.cubepanion.core.versionlinkers.FunctionLink;
import org.jetbrains.annotations.NotNull;
import javax.inject.Singleton;

@Singleton
@Implements(FunctionLink.class)
public class VersionedFunctionLink extends FunctionLink {

  @Override
  public void setCoolDown(@NotNull net.labymod.api.client.world.item.ItemStack itemStack, int duration) {
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
