package org.ccu.core.gui.hud.widgets;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Inventory;
import net.labymod.api.client.world.item.Item;
import net.labymod.api.client.world.item.ItemStack;
import org.ccu.core.gui.hud.widgets.base.CustomItemHudWidget;

public class HeldItemTracker extends CustomItemHudWidget {

  public HeldItemTracker() {
    super("held_item_tracker", "", 4, 1);
  }

  public void onTick() {
    ClientPlayer p = this.labyAPI.minecraft().clientPlayer();
    if (p == null) {
      this.itemStack = null;
      this.updateItemStack(null);
      this.updateItemName(null);
      return;
    }

    Inventory inv = p.inventory();
    if (inv == null) {
      this.itemStack = null;
      this.updateItemStack(null);
      this.updateItemName(null);
      return;
    }

    Item heldItem = p.getMainHandItemStack().getAsItem();
    if (heldItem.isAir()) {
      this.itemStack = p.getMainHandItemStack();
      this.updateItemStack(null);
      this.updateItemName(null);
      return;
    }

    this.counter = 0;
    for (int i = 0; i < 46; i++) {
      ItemStack itemStack = inv.itemStackAt(i);

      if (itemStack.getAsItem().equals(heldItem)) {
        this.counter += itemStack.getSize();
      }
    }

    this.itemStack = p.getMainHandItemStack();
    this.updateItemStack(this.itemStack);
    this.updateItemName(Component.text(this.counter));
  }
}
