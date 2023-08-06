package org.cubepanion.core.gui.hud.widgets;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.LivingEntity.EquipmentSpot;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.util.Color;
import org.cubepanion.core.gui.hud.widgets.base.CustomItemWidget;
import org.cubepanion.core.managers.CubepanionManager;

public class DurabilityItemHudWidget extends CustomItemWidget {

  private final CubepanionManager manager;
  private final EquipmentSpot equipmentSpot;

  public DurabilityItemHudWidget(HudWidgetCategory category, String id, String regex,
      EquipmentSpot spot, int posX, int posY, CubepanionManager manager) {
    super(id, regex, "", posX, posY);
    this.manager = manager;
    this.equipmentSpot = spot;

    this.bindCategory(category);
  }

  public void onTick(boolean inEditor) {
    if (inEditor) {
      Color colour = this.config.getTextColour().get();
      this.updateItemName(Component.text("1",
          TextColor.color(colour.getRed(), colour.getGreen(), colour.getBlue())), true);
      return;
    }
    ClientPlayer player = this.labyAPI.minecraft().getClientPlayer();
    if (player == null) {
      return;
    }
    this.itemIsHeld = false;
    ItemStack mainHand = player.getMainHandItemStack();
    ItemStack offHand = player.getOffHandItemStack();
    if ((mainHand.getAsItem().getIdentifier().getPath().contains(this.regex) || offHand.getAsItem()
        .getIdentifier().getPath().contains(this.regex))) {
      this.itemIsHeld = true;
    }
    this.counter = this.manager.getDurabilityManager().getDurability(this.equipmentSpot);
    this.updateItemName(Component.text(this.counter, this.config.getTextColor()), false);

  }

}
