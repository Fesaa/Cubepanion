package org.cubecraftutilities.v1_19_3;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.labymod.api.models.Implements;
import net.labymod.api.util.concurrent.task.Task;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
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
import org.cubecraftutilities.core.utils.VotingInterface;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Singleton
@Implements(VotingInterface.class)
public class VersionedVotingInterface extends VotingInterface {

  private LocalPlayer player = null;
  public ItemStack returnItemStack;

  private final Task starter = Task.builder(() -> {
    this.openVotingMenu(player, this.hotbarSlotIndex);
    this.waitForMenuOpenAndMakeFirstChoice(player);
  }).delay(300, TimeUnit.MILLISECONDS).build();

  @Inject
  public VersionedVotingInterface() {
  }

  @Override
  public void startAutoVote() {
    System.out.println("Starting outvote with " +
        String.format("Hotbar: %s, LeftChoice: %s, MiddleChoice: %s, RightChoice: %s, LeftVote: %s, MiddleVote: %s, RightVote: %s",
            this.hotbarSlotIndex, this.leftChoiceIndex, this.middleChoiceIndex, this.rightChoiceIndex, this.leftVoteIndex, this.middleVoteIndex, this.rightVoteIndex));
    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer player = minecraft.player;
    if (player == null) {
      return;
    }
    this.player = player;
    this.starter.run();

  }

  private void waitForMenuOpenAndMakeFirstChoice(@NotNull LocalPlayer player) {
    Timer timer = new Timer("waitForMenuOpenAndMakeFirstChoice");
    int choiceIndex = this.getNextChoiceIndex();
    if (choiceIndex == -1) {
      return;
    }
    int voteIndex = this.getNextVoteIndex();
    if (voteIndex == -1) {
      return;
    }
    VersionedVotingInterface votingInterface = this;
    timer.schedule(new TimerTask() {

      private int count = 0;
      @Override
      public void run() {
        if (count == 10) {
          System.out.println("waitForMenuOpenAndMakeFirstChoice canceled after 10 repetitions.");
          timer.cancel();
        }
        count++;
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof ChestMenu) {
          Slot slot = menu.getSlot(choiceIndex);
          if (votingInterface.clickOnSlot((ChestMenu) menu, slot)) {
            votingInterface.returnItemStack = menu.getSlot(votingInterface.returnIndex).getItem();
            votingInterface.waitForNewSlotAndClick(player, voteIndex, false);
          }
          timer.cancel();
        }
      }
    }, 100, 100);
  }

  private void waitForNewSlotAndClick(@NotNull LocalPlayer player, int index, boolean choice) {
    Timer timer = new Timer("waitForNewSlotAndClick");
    VersionedVotingInterface votingInterface = this;
    timer.schedule(new TimerTask() {
      private int count = 0;
      @Override
      public void run() {
        if (count == 10) {
          System.out.println("waitForNewSlotAndClick canceled after 10 repetitions.");
          timer.cancel();
        }
        count++;
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof ChestMenu) {
          Slot slot = menu.getSlot(votingInterface.returnIndex);
          if (slot.getItem().getDisplayName().equals(votingInterface.returnItemStack.getDisplayName())) {
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

          int nextIndex;
          if (choice) {
            nextIndex = votingInterface.getNextVoteIndex();
          } else {
            nextIndex = votingInterface.getNextChoiceIndex();
          }
          if (nextIndex == -1) {
            votingInterface.gracefulShutDown(player, (ChestMenu) menu);
          } else {
            votingInterface.waitForNewSlotAndClick(player, nextIndex, !choice);
          }
        }
      }
    }, 100, 100);
  }

  private void gracefulShutDown(@NotNull LocalPlayer player, @NotNull ChestMenu chest) {
    this.clickReturn(chest);
    Timer timer = new Timer("gracefulShutDown");
    VersionedVotingInterface votingInterface = this;
    timer.schedule(new TimerTask() {
      private int count = 0;
      @Override
      public void run() {
        if (count == 10) {
          System.out.println("gracefulShutDown canceled after 10 repetitions.");
          timer.cancel();
        }
        count++;
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof ChestMenu) {
          Slot slot = menu.getSlot(votingInterface.returnIndex);
          if (slot.getItem().getDisplayName().equals(votingInterface.returnItemStack.getDisplayName())) {
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
    Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors())
        .schedule(() -> {
          ClientPacketListener connection = Minecraft.getInstance().getConnection();
          if (connection == null) {
            return;
          }
          connection.send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, 0));
        },200, TimeUnit.MILLISECONDS);
  }
}
