package org.cubecraftutilities.core.gui.hud.widgets;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.LivingEntity.EquipmentSpot;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.world.item.ItemStack;
import org.cubecraftutilities.core.gui.hud.widgets.base.CustomItemWidget;
import org.cubecraftutilities.core.managers.CCUManager;

public class DurabilityItemHudWidget extends CustomItemWidget {

  private final CCUManager manager;
  private final EquipmentSpot equipmentSpot;

  public DurabilityItemHudWidget(HudWidgetCategory category, String id, String regex, EquipmentSpot spot, int posX, int posY, CCUManager manager) {
    super(id, regex, posX, posY);
    this.manager = manager;
    this.equipmentSpot = spot;

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
    this.itemIsHeld = false;
    ItemStack mainHand = player.getMainHandItemStack();
    ItemStack offHand = player.getOffHandItemStack();
    if ((mainHand.getAsItem().toString().contains(this.regex) || offHand.getAsItem().toString().contains(this.regex))) {
      this.itemIsHeld = true;
    }
    this.counter = this.manager.getDurabilityManager().getDurability(this.equipmentSpot);
    this.updateItemName(Component.text(this.counter));

  }

}
