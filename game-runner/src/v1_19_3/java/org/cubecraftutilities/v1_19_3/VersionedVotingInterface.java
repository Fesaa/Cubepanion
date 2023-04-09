package org.cubecraftutilities.v1_19_3;

import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
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

@Singleton
@Implements(VotingInterface.class)
public class VersionedVotingInterface extends VotingInterface {

  @Inject
  public VersionedVotingInterface() {};

  @Override
  public void voteEggWars(int mode, int health) {
    int votingIndex = 2;

    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer player = minecraft.player;
    if (player == null) {
      return;
    }

    this.tryToDropItem(player, votingIndex);
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        AbstractContainerMenu menu = player.containerMenu;
        System.out.println(menu);
        if (menu instanceof ChestMenu) {
          VersionedVotingInterface.clickOnSlot(player, (ChestMenu) menu, 12);
          timer.cancel();
        }
      }
    }, 100, 100);
  }

  private static void clickOnSlot(@NotNull LocalPlayer player, @NotNull ChestMenu chest, int index) {
    Slot slot = chest.getSlot(index);
    System.out.println(slot.getItem().getItem().getName(slot.getItem()).getString());
  }

  private void tryToDropItem(LocalPlayer player, int index) {
    Inventory inventory = player.getInventory();
    inventory.selected = index;
    ItemStack itemStack = inventory.getItem(index);
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        player.drop(itemStack, true);
        timer.cancel();
      }
    }, 200, 1);
  }
}
