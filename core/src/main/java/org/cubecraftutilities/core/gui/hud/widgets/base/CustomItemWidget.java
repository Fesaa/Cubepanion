package org.cubecraftutilities.core.gui.hud.widgets.base;

import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.GameMode;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.hud.hudwidget.text.TextHudWidgetConfig;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.ItemStack;

public class CustomItemWidget extends ItemHudWidget<TextHudWidgetConfig> {

  public final String regex;
  public boolean itemIsHeld;
  public int counter;
  private final int posX;
  private final int posY;

  protected CustomItemWidget(String id, String regex, int posX, int posY) {
    super(id);
    this.regex = regex;
    this.posX = posX;
    this.posY = posY;



    ResourceLocation resourceLocation = ResourceLocation.create("cubecraftutilities", "sprites.png");
    Icon icon = Icon.sprite16(resourceLocation, posX, posY);
    this.setIcon(icon);

    ItemStack item = Laby.references().itemStackFactory().create(ResourceLocation.create("minecraft", id));
    this.updateItemStack(item);
  }

  public boolean inventoryItemMatches(ItemStack itemStack, int i, int selectedEntry) {
    if (itemStack.getAsItem().toString().matches(this.regex)) {
      if (selectedEntry == i) {
        this.itemIsHeld = true;
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean isVisibleInGame() {
    Minecraft minecraft = this.labyAPI.minecraft();
    if (minecraft == null) {
      return false;
    } else {
      ClientPlayer player = minecraft.getClientPlayer();
      if (player != null && player.gameMode() != GameMode.SPECTATOR) {
        return this.counter > 0;
      } else {
        return false;
      }
    }
  }

  @Override
  public Icon createPlaceholderIcon() {
    ResourceLocation resourceLocation = ResourceLocation.create("cubecraftutilities", "sprites.png");
    return Icon.sprite16(resourceLocation, this.posX, this.posY);
  }
}
