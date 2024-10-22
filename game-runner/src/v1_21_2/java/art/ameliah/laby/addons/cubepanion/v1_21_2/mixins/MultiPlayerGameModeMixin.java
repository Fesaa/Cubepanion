package art.ameliah.laby.addons.cubepanion.v1_21_2.mixins;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import net.labymod.api.client.component.Component;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
    if (cubepanion$addon == null) {
      cubepanion$addon = Cubepanion.get();
    }
    if (!cubepanion$addon.configuration().getQolConfig().getNoDropSkyBlock().get()) {
      return;
    }
    if (!cubepanion$addon.getManager().getDivision().equals(CubeGame.SKYBLOCK)) {
      return;
    }
    if ($$3 != ClickType.THROW) {
      return;
    }
    AbstractContainerMenu inv = $$4.containerMenu;
    Slot slot;
    try {
      slot = inv.getSlot($$1);
    } catch (IndexOutOfBoundsException e) {
      LOGGER.debug(getClass(), e,
          "Ignoring handleInventoryMouseClick as the index is out of bounds");
      return;
    }
    ItemStack itemStack = slot.getItem();
    if ((itemStack.is(ItemTags.PICKAXES)
        || itemStack.is(ItemTags.AXES)
        || itemStack.is(ItemTags.SHOVELS)
        || itemStack.is(ItemTags.HOES)
        || itemStack.is(ItemTags.SWORDS)
        || itemStack.is(Items.BOW)
        || itemStack.getItem() instanceof ArmorItem)) {
      ci.cancel();
      cubepanion$addon.labyAPI().minecraft().chatExecutor().displayClientMessage(
          Component.translatable("cubepanion.messages.preventedDrop").color(Colours.Error), true);
    }
  }
}
