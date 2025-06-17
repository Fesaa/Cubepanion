package art.ameliah.laby.addons.cubepanion.v1_20_6;

import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import javax.inject.Singleton;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Singleton
@Implements(FunctionLink.class)
public class VersionedFunctionLink extends FunctionLink {

  @Override
  public void setCoolDown(@NotNull net.labymod.api.client.world.item.ItemStack itemStack,
      int duration) {
    if (Minecraft.getInstance().player == null) {
      return;
    }
    ItemStack mcItem = (ItemStack) (Object) itemStack;
    if (mcItem == null) {
      return;
    }
    Minecraft.getInstance().player.getCooldowns().addCooldown(mcItem.getItem(), duration);
  }

  @Override
  public void useItemInHotBar(int hotBarSlotIndex) {
    if (Minecraft.getInstance().gameMode == null || Minecraft.getInstance().player == null) {
      return;
    }

    Minecraft.getInstance().player.getInventory().selected = hotBarSlotIndex;
    Minecraft.getInstance().gameMode.useItem(Minecraft.getInstance().player, InteractionHand.MAIN_HAND);
  }

  @Override
  public void clickSlot(int slotId, int button) {
    if (Minecraft.getInstance().gameMode == null || Minecraft.getInstance().player == null) {
      return;
    }

    int containerId = Minecraft.getInstance().player.containerMenu.containerId;
    Minecraft.getInstance().gameMode.handleInventoryMouseClick(containerId, slotId, button, ClickType.PICKUP, Minecraft.getInstance().player);
  }

  @Override
  public CompletableFuture<List<CCItemStack>> loadMenuItems(Predicate<String> titlePredicate, Predicate<List<CCItemStack>> itemPredicate) {
    Minecraft minecraft = Minecraft.getInstance();
    Player player = minecraft.player;
    if (player == null) {
      return CompletableFuture.completedFuture(null);
    }

    return this.try10Times(1, () -> {
      Screen currenScreen = minecraft.screen;
      if (!(currenScreen instanceof ContainerScreen)) {
        return false;
      }

      AbstractContainerMenu menu = player.containerMenu;
      if ((!(menu instanceof ChestMenu))) {
        return false;
      }

      Component title = currenScreen.getTitle();
      var titleCheck = titlePredicate.test(title.getString());
      if (itemPredicate == null || !titleCheck) {
        return titleCheck;
      }

      List<CCItemStack> items = new ArrayList<>();
      for (var item : player.containerMenu.getItems()) {
        items.add((CCItemStack) (Object) item);
      }
      return itemPredicate.test(items);
    }, () -> {
      List<CCItemStack> items = new ArrayList<>();
      for (var item : player.containerMenu.getItems()) {
        items.add((CCItemStack) (Object) item);
      }
      return items;
    });
  }

}
