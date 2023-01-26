package org.ccu.core.gui.hud.widgets.base;

import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.mouse.MutableMouse;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.Item;
import net.labymod.api.client.world.item.ItemStack;

public class CustomItemHudWidget extends ItemHudWidget<ItemHudConfig> {

  public final Item item;
  public final String regex;
  public ItemStack itemStack;
  public final String id;
  public final Icon placeHolderIcon;
  public RenderableComponent renderableItemName;
  public Component itemName;
  public boolean itemIsHeld;
  public int counter;

  public CustomItemHudWidget(String id, String regex, int posX, int posY) {
    super(id, ItemHudConfig.class);
    this.id = id;
    this.regex = regex;
    this.item = Laby.references().itemStackFactory().create(ResourceLocation.create("minecraft", id)).getAsItem();
    ResourceLocation resourceLocation = ResourceLocation.create("ccu", "sprites.png");
    Icon icon = Icon.sprite16(resourceLocation, posX, posY);
    this.setIcon(icon);
    this.placeHolderIcon = icon;
  }


  private void updateName() {
    this.renderableItemName = this.itemName == null ? null : RenderableComponent.of(this.itemName);
  }

  @Override
  public void render(Stack stack, MutableMouse mouse, float partialTicks, boolean isEditorContext) {
    // Render item stack
    if (this.itemStack != null && !this.itemStack.isAir() && !isEditorContext) {
      this.labyAPI.minecraft()
          .itemStackRenderer()
          .renderItemStack(
              stack,
              this.itemStack,
              0,
              0,
              false
          );
    } else {
      this.placeHolderIcon.render(
          stack,
          0,
          0,
          ITEM_SIZE,
          ITEM_SIZE
      );
    }

    // Render item name
    if (this.renderableItemName != null) {
      float x = this.anchor.isRight()
          ? -this.renderableItemName.getWidth() - 2.0F
          : ITEM_SIZE + 2.0F;
      this.labyAPI.renderPipeline()
          .componentRenderer()
          .builder()
          .pos(x, ITEM_SIZE / 2.0F)
          .text(this.renderableItemName)
          .render(stack);
    }
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
  public void updateItemName(Component itemName) {
    this.itemName = itemName;
    this.updateName();
  }

  @Override
  public void load(ItemHudConfig config) {
    super.load(config);
    this.itemStack = Laby.references().itemStackFactory().create(this.item.getIdentifier());
    this.updateItemStack(this.itemStack);

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
    return (this.counter > 0) && (!this.config.getOnlyDisplayWhenHeld().get() || this.itemIsHeld) && this.itemStack != null && !this.itemStack.isAir();
  }


  @Override
  public Icon createPlaceholderIcon() {
    ResourceLocation resourceLocation =  ResourceLocation.create("ccu", "diamond.png");
    return Icon.sprite16(resourceLocation, 0, 0);
  }

}
