package org.ccu.core.HudWidgets;

import net.kyori.adventure.text.Component;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Inventory;
import net.labymod.api.client.gui.hud.hudwidget.HudWidgetConfig;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.Item;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.client.world.item.ItemStackFactory;
import net.labymod.api.inject.LabyGuice;

public class CustomItemHudWidget extends ItemHudWidget<HudWidgetConfig> {

  private final String id;
  private final Item item;
  private int count;

  private ItemStack itemStack;



  public CustomItemHudWidget(String id) {
    super(id);
    this.id = id;
    this.item = (((ItemStackFactory)LabyGuice.getInstance(ItemStackFactory.class)).create(
        ResourceLocation.create("minecraft", id))).getAsItem();
  }

  @Override
  public void load(HudWidgetConfig config) {
    super.load(config);
    this.itemStack = ((ItemStackFactory) LabyGuice.getInstance(ItemStackFactory.class)).create(
        this.item.getIdentifier());
    this.updateItemStack(this.itemStack);

  }

  public void onTick() {

    ClientPlayer player = this.labyAPI.minecraft().clientPlayer();
    if (player != null) {
      Inventory inventory = player.inventory();
      ItemStack biggestItemStack = null;
      this.count = 0;
      for (int i = 0; i < 46; i++) {
        ItemStack iStack = inventory.itemStackAt(i);

        String[] s = iStack.getAsItem().toString().split(" ");

        if (s[s.length-1].equals(this.id)) {
          this.count += iStack.getSize();
          if (biggestItemStack == null ||biggestItemStack.getSize() < iStack.getSize()) {
            biggestItemStack = iStack;
          }
        }
        this.itemStack = biggestItemStack;
      }
    }
    this.updateItemStack(this.itemStack);
    this.updateItemName(Component.text(this.count));
  }

  public boolean isVisibleInGame() {
    Minecraft minecraft = this.labyAPI.minecraft();
    if (minecraft == null) {
      return false;
    }

    ClientPlayer player = minecraft.clientPlayer();
    if (player == null) {
      return false;
    }

    return this.count > 0;
  }

  @Override
  public Icon createPlaceholderIcon() {
    return Icon.sprite16(ResourceLocation.create("minecraft", id), 6, 0);
  }



}
