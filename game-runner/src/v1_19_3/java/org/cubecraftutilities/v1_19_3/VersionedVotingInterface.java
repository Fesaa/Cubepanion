package org.cubecraftutilities.v1_19_3;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.labymod.api.models.Implements;
import net.labymod.api.util.concurrent.task.Task;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
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
import java.util.concurrent.TimeUnit;

@Singleton
@Implements(VotingInterface.class)
public class VersionedVotingInterface extends VotingInterface {
  private int hotbarSlotIndex;
  private int leftChoiceIndex;
  private int leftVoteIndex;
  private int middleChoiceIndex;
  private int middleVoteIndex;
  private int rightChoiceIndex;
  private int rightVoteIndex;

  public ItemStack lastClickedItem;
  public int lastIndex;
  private LocalPlayer player = null;

  private static VersionedVotingInterface instance;

  private final Task starter = Task.builder(() -> {
    this.openVotingMenu(player, this.hotbarSlotIndex);
    this.waitForMenuOpenAndMakeFirstChoice(player);
  }).delay(300, TimeUnit.MILLISECONDS).build();

  @Inject
  public VersionedVotingInterface() {
    instance = this;
  }

  public static VersionedVotingInterface getInstance() {
    return instance;
  }

  @Override
  public void voteEggWars(int mode, int health) {
    this.hotbarSlotIndex = 2;
    this.leftChoiceIndex = 12;
    this.leftVoteIndex = mode;
    this.middleChoiceIndex = -1;
    this.middleVoteIndex = -1;
    this.rightChoiceIndex = 14;
    this.rightVoteIndex = health;

    this.startAutoVote();
  }

  @Override
  public void voteSkyWars(int mode, int projectiles, int time) {
    this.hotbarSlotIndex = 1;
    this.leftChoiceIndex = 11;
    this.leftVoteIndex = mode;
    this.middleChoiceIndex = 13;
    this.middleVoteIndex = projectiles;
    this.rightChoiceIndex = 15;
    this.rightVoteIndex = time;

    this.startAutoVote();
  }

  @Override
  public void voteLuckyIslands(int mode, int time) {
    this.hotbarSlotIndex = 1;
    this.leftChoiceIndex = 12;
    this.leftVoteIndex = mode;
    this.middleChoiceIndex = -1;
    this.middleVoteIndex = -1;
    this.rightChoiceIndex = 14;
    this.rightVoteIndex = time;

    this.startAutoVote();
  }

  private void startAutoVote() {
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
    VersionedVotingInterface votingInterface = VersionedVotingInterface.getInstance();
    timer.schedule(new TimerTask() {

      private int count = 0;
      @Override
      public void run() {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof ChestMenu) {
          Slot slot = menu.getSlot(choiceIndex);
          boolean success = votingInterface.clickOnSlot((ChestMenu) menu, slot);
          timer.cancel();
          if (success) {
            votingInterface.lastClickedItem = slot.getItem();
            votingInterface.lastIndex = slot.index;
            votingInterface.waitForNewSlotAndClick(player, voteIndex, false);
          }
        }
        if (count == 10) {
          timer.cancel();
        }
        count++;
      }
    }, 100, 100);
  }

  private void waitForNewSlotAndClick(@NotNull LocalPlayer player, int index, boolean choice) {
    Timer timer = new Timer("waitForNewSlotAndClick");
    VersionedVotingInterface votingInterface = VersionedVotingInterface.getInstance();
    timer.schedule(new TimerTask() {
      private int count = 0;
      @Override
      public void run() {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof ChestMenu) {
          Slot slot = menu.getSlot(votingInterface.lastIndex);
          if (slot.getItem().sameItem(votingInterface.lastClickedItem)) {
            return;
          }

          timer.cancel();
          if (!votingInterface.clickOnSlot((ChestMenu) menu, menu.getSlot(index))) {
            return;
          }
          votingInterface.lastClickedItem = slot.getItem();
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
        if (count == 10) {
          timer.cancel();
        }
        count++;
      }
    }, 100, 100);
  }

  private void gracefulShutDown(@NotNull LocalPlayer player, @NotNull ChestMenu chest) {
    this.clickReturn(chest);
    Timer timer = new Timer("gracefulShutDown");
    VersionedVotingInterface votingInterface = VersionedVotingInterface.getInstance();
    timer.schedule(new TimerTask() {
      private int count = 0;
      @Override
      public void run() {
        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof ChestMenu) {
          Slot slot = menu.getSlot(votingInterface.lastIndex);
          if (slot.getItem().sameItem(votingInterface.lastClickedItem)) {
            return;
          }
          votingInterface.clickReturn((ChestMenu) menu);
          timer.cancel();

        }
        if (count == 10) {
          timer.cancel();
        }
        count++;
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
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) {
          return;
        }
        connection.send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
        timer.cancel();
      }
    }, 500, 1);
  }

  private int getNextChoiceIndex() {
    int temp;
    if (this.leftChoiceIndex != -1) {
      temp = this.leftChoiceIndex;
      this.leftChoiceIndex = -1;
    } else if (this.middleChoiceIndex != -1) {
      temp = this.middleChoiceIndex;
      this.middleVoteIndex = -1;
    } else {
      temp = this.rightChoiceIndex;
      this.rightChoiceIndex = -1;
    }
    return temp;
  }

  private int getNextVoteIndex() {
    int temp;
    if (this.leftVoteIndex != -1) {
      temp = this.leftVoteIndex;
      this.leftVoteIndex = -1;
    } else if (this.middleVoteIndex != -1) {
      temp = this.middleVoteIndex;
      this.middleVoteIndex = -1;
    } else {
      temp = this.rightVoteIndex;
      this.rightVoteIndex = -1;
    }
    return temp;
  }
}
