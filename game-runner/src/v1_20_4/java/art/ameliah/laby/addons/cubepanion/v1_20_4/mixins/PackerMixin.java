package art.ameliah.laby.addons.cubepanion.v1_20_4.mixins;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.ItemUseEvent;
import art.ameliah.laby.addons.cubepanion.core.events.ItemUseEvent.Hand;
import art.ameliah.laby.addons.cubepanion.core.events.ItemUseEvent.UseType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class PackerMixin {

  @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/protocol/Packet;)V")
  public void onSend(Packet<?> packet, CallbackInfo ci) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
      return;
    }
    if (packet instanceof ServerboundUseItemPacket p) {
      Hand hand = p.getHand() == InteractionHand.MAIN_HAND ? Hand.MAIN_HAND : Hand.OFF_HAND;
      cubepanion$fireEvent(UseType.USE, hand, mc.player.getItemInHand(p.getHand()));
    } else if (packet instanceof ServerboundUseItemOnPacket p) {
      Hand hand = p.getHand() == InteractionHand.MAIN_HAND ? Hand.MAIN_HAND : Hand.OFF_HAND;
      cubepanion$fireEvent(UseType.USE, hand, mc.player.getItemInHand(p.getHand()));
    } else if (packet instanceof ServerboundSwingPacket p) {
      Hand hand = p.getHand() == InteractionHand.MAIN_HAND ? Hand.MAIN_HAND : Hand.OFF_HAND;
      cubepanion$fireEvent(UseType.SWING, hand, mc.player.getItemInHand(p.getHand()));
    }
  }

  private void cubepanion$fireEvent(UseType type, Hand hand, ItemStack item) {
    ItemUseEvent e = new ItemUseEvent(type,
        (net.labymod.api.client.world.item.ItemStack) (Object) item, hand);
    Cubepanion.get().labyAPI().eventBus().fire(e);
  }

}
