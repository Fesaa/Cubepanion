package art.ameliah.laby.addons.cubepanion.v1_21_2;

import art.ameliah.laby.addons.cubepanion.core.versionlinkers.QOLMapSelectorLink;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Blocks;

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
            List<Component> hoverText = itemStack.getTooltipLines(
                TooltipContext.of(Minecraft.getInstance().level),
                player, TooltipFlag.NORMAL);
            if (hoverText.size() < 4) {
              break;
            }
            int gameCount = -1;
            Component currentGamesComponent = hoverText.get(2);
            List<Component> currentGamesSiblings = currentGamesComponent.getSiblings();
            if (!currentGamesSiblings.isEmpty()) {
              currentGamesSiblings = currentGamesSiblings.get(0).getSiblings();
              if (currentGamesSiblings.size() >= 4) {
                String gameCountString = currentGamesSiblings.get(3).getString();
                try {
                  gameCount = Integer.parseInt(gameCountString);
                } catch (NumberFormatException e) {
                  continue;
                }
              }
            }

            ItemStack newItemStack = new ItemStack(Blocks.RED_STAINED_GLASS_PANE);
            newItemStack.set(DataComponents.CUSTOM_NAME, itemStack.getHoverName());
            List<Component> lines = itemStack.getTooltipLines(
                TooltipContext.of(Minecraft.getInstance().level), player, TooltipFlag.NORMAL);
            if (!lines.isEmpty()) {
              lines.removeFirst();
            }
            ItemLore itemLore = new ItemLore(lines);
            newItemStack.set(DataComponents.LORE, itemLore);

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
