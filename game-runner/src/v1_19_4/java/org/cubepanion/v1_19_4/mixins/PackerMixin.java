package org.cubepanion.v1_19_4.mixins;

import net.labymod.v1_19_4.client.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.events.ItemUseEvent;
import org.cubepanion.core.events.ItemUseEvent.Hand;
import org.cubepanion.core.events.ItemUseEvent.UseType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class PackerMixin {

  @Shadow
  public abstract void send(Packet<?> $$0);

  @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/protocol/Packet;)V")
  public void onSend(Packet<?> packet, CallbackInfo ci) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
      return;
    } else {
        mc.player.getInventory();
    }
    ItemStack selected = mc.player.getInventory().getSelected();
    if (packet instanceof ServerboundUseItemPacket usePacket) {
      Hand hand = usePacket.getHand() == InteractionHand.MAIN_HAND ? Hand.MAIN_HAND : Hand.OFF_HAND;
      ItemUseEvent e = new ItemUseEvent(UseType.USE, ItemUtil.getLabyItemStack(selected), hand);
      Cubepanion.get().labyAPI().eventBus().fire(e);

    } else if (packet instanceof ServerboundSwingPacket swingPacket) {
      Hand hand = swingPacket.getHand() == InteractionHand.MAIN_HAND ? Hand.MAIN_HAND : Hand.OFF_HAND;
      ItemUseEvent e = new ItemUseEvent(UseType.SWING, ItemUtil.getLabyItemStack(selected), hand);
      Cubepanion.get().labyAPI().eventBus().fire(e);
    }



  }

}
