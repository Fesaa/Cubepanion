package art.ameliah.laby.addons.cubepanion.v1_20_4;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.VotingLink;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.labymod.api.util.concurrent.task.Task;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Singleton
@Implements(VotingLink.class)
public class VersionedVotingLink extends VotingLink {

  private final Task clickOnMenu = Task.builder(() -> {
    ClientPacketListener connection = Minecraft.getInstance().getConnection();
    if (connection == null) {
      LOGGER.info(getClass(), "Connection was null");
      return;
    }
    LOGGER.debug(true, this.getClass(), "Opening menu");
    int sequence = 0;
    if (Cubepanion.get().configuration().getAutoVoteSubConfig().getExperiments().get()) {
      BlockStatePredictionHandler handler = connection.getLevel().getBlockStatePredictionHandler();
      handler.startPredicting();
      sequence = handler.currentSequence();
    }
    connection.send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, sequence));
  }).delay(200, TimeUnit.MILLISECONDS).build();
  public ItemStack returnItemStack;
  private LocalPlayer player = null;
  private final Task starter = Task.builder(() -> {
    this.returnItemStack = null;
    this.openVotingMenu(player, this.hotbarSlotIndex);

    VotePair votePair = this.getNextVotePair();
    if (votePair.choiceIndex() == -1) {
      this.waitForNewSlotAndClick(player, votePair, false);
    } else {
      this.waitForMenuOpenAndMakeFirstChoice(player, votePair);
    }
  }).delay(100, TimeUnit.MILLISECONDS).build();

  @Inject
  public VersionedVotingLink() {
  }

  @Override
  public void startAutoVote() {
    LOGGER.info(getClass(), "Starting autovote");
    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer player = minecraft.player;
    if (player == null) {
      return;
    }
    this.player = player;
    this.starter.execute();

  }

  private void waitForMenuOpenAndMakeFirstChoice(@NotNull LocalPlayer player, VotePair votePair) {
    Timer timer = new Timer("waitForMenuOpenAndMakeFirstChoice");
    LOGGER.debug(this.getClass(), "Starting vote with pair:", votePair);
    VersionedVotingLink votingInterface = this;
    timer.schedule(new TimerTask() {

      private int count = 0;

      @Override
      public void run() {
        if (count == 10) {
          timer.cancel();
        }
        count++;
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof ChestMenu) {
          Slot slot = menu.getSlot(votePair.choiceIndex());
          if (votingInterface.clickOnSlot((ChestMenu) menu, slot)) {
            votingInterface.returnItemStack = menu.getSlot(votingInterface.returnIndex).getItem();
            votingInterface.waitForNewSlotAndClick(player, votePair, false);
          }
          timer.cancel();
        }
      }
    }, 100, 100);
  }

  private void waitForNewSlotAndClick(@NotNull LocalPlayer player, VotePair votePair,
      boolean choice) {
    int index = choice ? votePair.choiceIndex() : votePair.voteIndex();
    Timer timer = new Timer("waitForNewSlotAndClick");
    VersionedVotingLink votingInterface = this;
    timer.schedule(new TimerTask() {
      private int count = 0;

      @Override
      public void run() {
        if (count == 10) {
          timer.cancel();
        }
        count++;
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof ChestMenu) {
          Slot slot = menu.getSlot(votingInterface.returnIndex);
          if (votingInterface.returnItemStack != null && slot.getItem().getDisplayName()
              .equals(votingInterface.returnItemStack.getDisplayName())) {
            return;
          }

          timer.cancel();
          if (!votingInterface.clickOnSlot((ChestMenu) menu, menu.getSlot(index))) {
            return;
          }
          votingInterface.returnItemStack = slot.getItem();
          if (!votingInterface.clickReturn((ChestMenu) menu)) {
            return;
          }

          VotePair nextVotePair = choice ? votePair : votingInterface.getNextVotePair();
          LOGGER.debug(this.getClass(), "Voting with pair:", votePair);
          int nextIndex = !choice ? nextVotePair.choiceIndex() : nextVotePair.voteIndex();
          if (nextIndex == -1) {
            votingInterface.gracefulShutDown(player, (ChestMenu) menu);
          } else {
            votingInterface.waitForNewSlotAndClick(player, nextVotePair, !choice);
          }
        }
      }
    }, 100, 100);
  }

  private void gracefulShutDown(@NotNull LocalPlayer player, @NotNull ChestMenu chest) {
    this.clickReturn(chest);
    Timer timer = new Timer("gracefulShutDown");
    VersionedVotingLink votingInterface = this;
    timer.schedule(new TimerTask() {
      private int count = 0;

      @Override
      public void run() {
        if (count == 10) {
          timer.cancel();
        }
        count++;
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof ChestMenu) {
          Slot slot = menu.getSlot(votingInterface.returnIndex);
          if (slot.getItem().getDisplayName()
              .equals(votingInterface.returnItemStack.getDisplayName())) {
            return;
          }
          votingInterface.clickReturn((ChestMenu) menu);
          timer.cancel();

        }
      }
    }, 200, 10);
  }

  private boolean clickOnSlot(@NotNull ChestMenu chest, Slot slot) {
    ClientPacketListener connection = Minecraft.getInstance().getConnection();
    if (connection == null) {
      return false;
    }
    LOGGER.debug(true, this.getClass(), "Clicking on slot", slot, "with item", slot.getItem());
    connection.send(
        new ServerboundContainerClickPacket(
            chest.containerId,
            chest.getStateId(),
            slot.getContainerSlot(),
            0,
            ClickType.PICKUP,
            slot.getItem(),
            new Int2ObjectOpenHashMap<>()
        )
    );
    return true;
  }

  private boolean clickReturn(@NotNull ChestMenu chest) {
    return this.clickOnSlot(chest, chest.getSlot(31));
  }

  private void openVotingMenu(@NotNull LocalPlayer player, int index) {
    Inventory inventory = player.getInventory();
    inventory.selected = index;
    this.clickOnMenu.execute();
  }
}
