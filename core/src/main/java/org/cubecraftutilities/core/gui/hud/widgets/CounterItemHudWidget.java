package org.cubecraftutilities.core.gui.hud.widgets;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Inventory;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.util.Color;
import org.cubecraftutilities.core.gui.hud.widgets.base.CustomItemWidget;
import java.awt.*;

public class CounterItemHudWidget extends CustomItemWidget {

  public CounterItemHudWidget(HudWidgetCategory category, String id, String regex, int posX, int posY) {
    super(id, regex, posX, posY);

    this.bindCategory(category);
  }

  @Override
  public void onTick(boolean inEditor) {
    if (inEditor) {
      Color colour = this.config.getTextColour().get();
      this.updateItemName(Component.text("1",
          TextColor.color(colour.getRed(), colour.getGreen(), colour.getBlue())));
      return;
    }
    ClientPlayer player = this.labyAPI.minecraft().getClientPlayer();
    if (player != null) {
      ItemStack itemStack = null;
      ItemStack offHandItem = player.getOffHandItemStack();
      Inventory inventory = player.inventory();
      int selectedEntry = inventory.getSelectedIndex();

      this.counter = 0;
      this.itemIsHeld = false;
      for (int i = 0; i < 46; i++) {
        ItemStack iStack = inventory.itemStackAt(i);
        if (this.inventoryItemMatches(iStack, i, selectedEntry)) {
          this.counter += iStack.getSize();
          itemStack = iStack;
        }
      }

      if (this.inventoryItemMatches(offHandItem, -1, selectedEntry)) {
        this.counter += offHandItem.getSize();
      }
      this.updateItemStack(itemStack);
      Color colour = this.config.getTextColour().get();
      this.updateItemName(Component.text(this.counter,
          TextColor.color(colour.getRed(), colour.getGreen(), colour.getBlue())));
    }
  }

}
