package org.cubecraftutilities.core.gui.hud.widgets;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Inventory;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.world.item.Item;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.util.Color;
import org.cubecraftutilities.core.gui.hud.widgets.base.CustomItemWidget;

public class HeldItemTracker extends CustomItemWidget {

  public HeldItemTracker(HudWidgetCategory category) {
    super("held_item_tracker", "", 4, 1);

    this.bindCategory(category);
  }

  public void onTick(boolean inEditor) {
    if (inEditor) {
      Color colour = this.config.getTextColour().get();
      this.updateItemName(Component.text("1",
          TextColor.color(colour.getRed(), colour.getGreen(), colour.getBlue())));
      return;
    }
    ClientPlayer p = this.labyAPI.minecraft().getClientPlayer();
    if (p == null) {
      return;
    }

    Inventory inv = p.inventory();
    if (inv == null) {
      return;
    }

    ItemStack heldItemStack = p.getMainHandItemStack();
    Item heldItem = heldItemStack.getAsItem();
    this.counter = 0;
    if (heldItem.isAir()) {
      return;
    }
    for (int i = 0; i < 46; i++) {
      ItemStack itemStack = inv.itemStackAt(i);
      if (itemStack.getAsItem().equals(heldItem)) {
        this.counter += itemStack.getSize();
      }
    }
    this.updateItemStack(heldItemStack);
    Color colour = this.config.getTextColour().get();
    this.updateItemName(Component.text(this.counter,
        TextColor.color(colour.getRed(), colour.getGreen(), colour.getBlue())));
  }
}
