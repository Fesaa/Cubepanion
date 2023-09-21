package org.cubepanion.v1_19_2.mixins;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.CubeGame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

  @Unique
  private Cubepanion cubepanion$addon = null;

  @Inject(at = @At("HEAD"), method = "handleInventoryMouseClick", cancellable = true)
  private void handleInventoryMouseClick(int $$0, int $$1, int $$2, ClickType $$3, Player $$4,
      CallbackInfo ci) {
    if ($$3 != ClickType.THROW) {
      return;
    }
    if (cubepanion$addon == null) {
      cubepanion$addon = Cubepanion.get();
    }
    AbstractContainerMenu inv = $$4.containerMenu;
    Slot slot = inv.getSlot($$1);
    ItemStack itemStack = slot.getItem();

    if ((itemStack.getItem() instanceof TieredItem || itemStack.is(Items.BOW)
        || itemStack.getItem() instanceof ArmorItem)
        && cubepanion$addon.configuration().getQolConfig().getNoDropSkyBlock().get()
        && cubepanion$addon.getManager().getDivision().equals(CubeGame.SKYBLOCK)) {
      ci.cancel();
    }
  }
}
