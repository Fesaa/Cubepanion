package org.cubepanion.core.gui.hud.widgets;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Inventory;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.world.item.ItemStack;
import org.cubepanion.core.gui.hud.widgets.base.CustomItemWidget;

public class CounterItemHudWidget extends CustomItemWidget {

  public CounterItemHudWidget(HudWidgetCategory category, String id, String regex, String itemName,
      int posX, int posY) {
    super(id, regex, itemName, posX, posY);

    this.bindCategory(category);
  }

  @Override
  public void onTick(boolean inEditor) {
    if (inEditor) {
      this.updateItemName(Component.text("1", this.config.getTextColor()), true);
      return;
    }
    ClientPlayer player = this.labyAPI.minecraft().getClientPlayer();
    if (player != null) {
      ItemStack offHandItem = player.getOffHandItemStack();
      Inventory inventory = player.inventory();
      int selectedEntry = inventory.getSelectedIndex();

      int innerCounter = 0;
      this.itemIsHeld = false;
      for (int i = 0; i < 46; i++) {
        ItemStack iStack = inventory.itemStackAt(i);
        if (this.inventoryItemMatches(iStack, i == selectedEntry)) {
          innerCounter += iStack.getSize();
        }
      }

      if (this.inventoryItemMatches(offHandItem)) {
        innerCounter += offHandItem.getSize();
      }
      this.counter = innerCounter;
      this.updateItemStack(item, false);
      this.updateItemName(Component.text(this.counter, this.config.getTextColor()), false);
    }
  }

}
