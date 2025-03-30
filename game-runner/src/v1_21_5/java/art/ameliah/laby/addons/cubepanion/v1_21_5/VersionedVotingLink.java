package art.ameliah.laby.addons.cubepanion.v1_21_5;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.VotingLink;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.HashedStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ClickType;

@Singleton
@Implements(VotingLink.class)
public class VersionedVotingLink extends VotingLink {

  @Inject
  public VersionedVotingLink() {
  }

  @Override
  public void openMenu(int hotbarSlotIndex) {
    LocalPlayer player = Minecraft.getInstance().player;
    ClientPacketListener connection = Minecraft.getInstance().getConnection();
    if (player == null || connection == null) {
      return;
    }

    player.getInventory().setSelectedSlot(hotbarSlotIndex);
    connection.send(new ServerboundSetCarriedItemPacket(hotbarSlotIndex));
    int sequence = 0;
    if (Cubepanion.get().configuration().getAutoVoteSubConfig().getExperiments().get()) {
      BlockStatePredictionHandler handler = connection.getLevel().getBlockStatePredictionHandler();
      handler.startPredicting();
      sequence = handler.currentSequence();
    }
    connection.send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, sequence, player.getXRot(), player.getYRot()));

  }

  @Override
  public void clickSlot(int syncId, int slotId, int button) {
    if (slotId <  Short.MIN_VALUE || slotId > Short.MAX_VALUE) {
      LOGGER.warn(getClass(), "Tried to click on a slot with id that doesn't fit inside a short!, {}", slotId);
      return;
    }
    
    ClientPacketListener connection = Minecraft.getInstance().getConnection();
    if (connection == null) {
      return;
    }
    connection.send(
        new ServerboundContainerClickPacket(
            syncId,
            0,
            (short) slotId,
            (byte) 0,
            ClickType.PICKUP,
            new Int2ObjectOpenHashMap<>(),
            HashedStack.EMPTY
        )
    );
  }
}

