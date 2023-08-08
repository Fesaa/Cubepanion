package org.cubepanion.v1_19_3;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Blocks;
import org.cubepanion.core.versionlinkers.QOLMapSelectorLink;

@Singleton
@Implements(QOLMapSelectorLink.class)
public class VersionedQOLMapSelectorLink extends QOLMapSelectorLink {

  @Inject
  public VersionedQOLMapSelectorLink() {

  }

  @Override
  public void onScreenOpen() {
    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    if (player == null) {
      return;
    }
    VersionedQOLMapSelectorLink mapSelector = this;

    Timer timer = new Timer("onScreenOpen");
    timer.schedule(new TimerTask() {

      private int count = 0;

      @Override
      public void run() {
        if (count == 10) {
          timer.cancel();
        }
        count++;
        Screen currenScreen = minecraft.screen;
        if (!(currenScreen instanceof ContainerScreen)) {
          return;
        }
        Component title = currenScreen.getTitle();
        if (!title.toString().contains("Map selection")) {
          return;
        }

        AbstractContainerMenu menu = player.containerMenu;
        if (menu instanceof ChestMenu) {
          for (Slot slot : menu.slots) {
            ItemStack itemStack = slot.getItem();
            if (itemStack.isEmpty()) {
              break;
            }
            List<Component> hoverText = itemStack.getTooltipLines(player, TooltipFlag.NORMAL);
            if (hoverText.size() != 4) {
              break;
            }
            int gameCount = -1;
            Component currentGamesComponent = hoverText.get(2);
            List<Component> currentGamesSiblings = currentGamesComponent.getSiblings();
            if (!currentGamesSiblings.isEmpty()) {
              currentGamesSiblings = currentGamesSiblings.get(0).getSiblings();
              if (currentGamesSiblings.size() == 4) {
                String gameCountString = currentGamesSiblings.get(3).getString();
                try {
                  gameCount = Integer.parseInt(gameCountString);
                } catch (NumberFormatException e) {
                  continue;
                }
              }
            }

            ItemStack newItemStack = new ItemStack(Blocks.RED_STAINED_GLASS_PANE);
            newItemStack.setHoverName(itemStack.getHoverName());

            if (gameCount == 0) {
              menu.setItem(slot.index, menu.getStateId(), newItemStack);
            }
          }
          timer.cancel();
        }
      }

    }, 100, 100);

  }
}
