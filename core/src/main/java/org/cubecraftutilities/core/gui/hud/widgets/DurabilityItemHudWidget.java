package org.cubecraftutilities.core.gui.hud.widgets;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.LivingEntity.EquipmentSpot;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Inventory;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.world.item.ItemStack;
import org.cubecraftutilities.core.gui.hud.widgets.base.CustomItemWidget;
import org.cubecraftutilities.core.managers.CCUManager;
import org.cubecraftutilities.core.managers.submanagers.DurabilityManager;

public class DurabilityItemHudWidget extends CustomItemWidget {

  private final CCUManager manager;

  public DurabilityItemHudWidget(HudWidgetCategory category, String id, String regex, int posX, int posY, CCUManager manager) {
    super(id, regex, posX, posY);
    this.manager = manager;

    this.bindCategory(category);
  }

  public void onTick(boolean inEditor) {
    if (inEditor) {
      this.updateItemName(Component.text("1"));
      return;
    }
    ClientPlayer player = this.labyAPI.minecraft().getClientPlayer();
    if (player == null) {
      return;
    }

    this.counter = 0;
    this.itemIsHeld = false;

    ItemStack helmet = player.getEquipmentItemStack(EquipmentSpot.HEAD);
    ItemStack chestPlate = player.getEquipmentItemStack(EquipmentSpot.CHEST);
    ItemStack leggings = player.getEquipmentItemStack(EquipmentSpot.LEGS);
    ItemStack boots = player.getEquipmentItemStack(EquipmentSpot.FEET);
    ItemStack offHand = player.getOffHandItemStack();

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
    if (this.inventoryItemMatches(offHand, -1, selectedEntry)) {
      this.counter += (offHand.getMaximumDamage() - offHand.getCurrentDamageValue());
    }
    this.updateItemName(Component.text(this.counter));

    this.updateCCUInternalConfig();
  }

  private void updateCCUInternalConfig() {
    DurabilityManager durabilityManager = this.manager.getDurabilityManager();
    switch (this.id) {
      case "helmet_durability_counter": {
        durabilityManager.setTotalHelmetDurability(this.counter);
        break;
      }
      case "chestplate_durability_counter": {
        durabilityManager.setTotalChestPlateDurability(this.counter);
        break;
      }
      case "leggings_durability_counter": {
        durabilityManager.setTotalLeggingsDurability(this.counter);
        break;
      }
      case "boots_durability_counter": {
        durabilityManager.setTotalBootsDurability(this.counter);
        break;
      }
    }
  }

}
