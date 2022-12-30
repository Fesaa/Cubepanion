package org.ccu.core.gui.hud.widgets;

import net.kyori.adventure.text.Component;
import net.labymod.api.client.entity.LivingEntity.EquipmentSpot;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Inventory;
import net.labymod.api.client.world.item.ItemStack;
import org.ccu.core.config.internal.CCUinternalConfig;
import org.ccu.core.gui.hud.widgets.base.CustomItemHudWidget;

public class DurabilityItemHudWidget extends CustomItemHudWidget {

  public DurabilityItemHudWidget(String id, String regex, int posX, int posY) {
    super(id, regex, posX, posY);
  }

  public void onTick() {
    ClientPlayer player = this.labyAPI.minecraft().clientPlayer();
    if (player == null) {
      return;
    }

    this.counter = 0;
    this.itemIsHeld = false;

    ItemStack helmet = player.getEquipmentItemStack(EquipmentSpot.HEAD);
    ItemStack chestPlate = player.getEquipmentItemStack(EquipmentSpot.CHEST);
    ItemStack leggings = player.getEquipmentItemStack(EquipmentSpot.LEGS);
    ItemStack boots = player.getEquipmentItemStack(EquipmentSpot.FEET);

    Inventory inventory = player.inventory();
    int selectedEntry = inventory.getSelectedIndex();
    for (int i = 0; i < 46; i++) {
      ItemStack iStack = inventory.itemStackAt(i);
      if (this.inventoryItemMatches(iStack, i, selectedEntry)) {
        this.counter += (iStack.getMaximumDamage() - iStack.getCurrentDamageValue());
      }
    }
    if (this.inventoryItemMatches(helmet, -1, selectedEntry)) {
      this.counter += (helmet.getMaximumDamage() - helmet.getCurrentDamageValue());
    }
    if (this.inventoryItemMatches(chestPlate, -1, selectedEntry)) {
      this.counter += (chestPlate.getMaximumDamage() - chestPlate.getCurrentDamageValue());
    }
    if (this.inventoryItemMatches(leggings, -1, selectedEntry)) {
      this.counter += (leggings.getMaximumDamage() - leggings.getCurrentDamageValue());
    }
    if (this.inventoryItemMatches(boots, -1, selectedEntry)) {
      this.counter += (boots.getMaximumDamage() - boots.getCurrentDamageValue());
    }

    this.updateItemStack(this.itemStack);
    this.updateItemName(Component.text(this.counter));

    this.updateCCUInternalConfig();
  }

  private void updateCCUInternalConfig() {
    switch (this.id) {
      case "helmet_durability_counter": {
        CCUinternalConfig.totalHelmetDurability = this.counter;
        break;
      }
      case "chestplate_durability_counter": {
        CCUinternalConfig.totalChestPlateDurability = this.counter;
        break;
      }
      case "leggings_durability_counter": {
        CCUinternalConfig.totalLeggingsDurability = this.counter;
        break;
      }
      case "boots_durability_counter": {
        CCUinternalConfig.totalBootsDurability = this.counter;
        break;
      }
    }
  }

}
