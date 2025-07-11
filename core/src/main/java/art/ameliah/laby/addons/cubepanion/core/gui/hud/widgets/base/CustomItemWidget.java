package art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.base;

import art.ameliah.laby.addons.cubepanion.core.listener.hud.HudEvents;
import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.GameMode;
import net.labymod.api.client.gui.hud.hudwidget.item.ItemHudWidget;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.ItemStack;

public class CustomItemWidget extends ItemHudWidget<ItemHudConfig> {

  public final String regex;
  private final int posX;
  private final int posY;
  public boolean itemIsHeld;
  public int counter;
  public ItemStack item;

  protected CustomItemWidget(String id, String regex, String itemName, int posX, int posY) {
    super(id, ItemHudConfig.class);
    this.regex = regex;
    this.posX = posX;
    this.posY = posY;
    this.itemIsHeld = false;


    var src = ResourceLocation.create("minecraft", itemName);
    // If no regex is passed, use the item as resource
    if (this.regex == null || this.regex.isEmpty()) {
      this.setIcon(Icon.texture(src));
    } else {
      ResourceLocation resourceLocation = ResourceLocation.create("cubepanion", "sprites.png");
      Icon icon = Icon.sprite16(resourceLocation, posX, posY);
      this.setIcon(icon);
    }

    this.item = Laby.references().itemStackFactory().create(src);
  }

  public void load(ItemHudConfig config) {
    super.load(config);
  }

  @Override
  public boolean isVisibleInGame() {
    Minecraft minecraft = this.labyAPI.minecraft();
    if (minecraft == null) {
      return false;
    } else {
      ClientPlayer player = minecraft.getClientPlayer();
      if (player != null && player.gameMode() != GameMode.SPECTATOR && this.counter > 0) {
        HudEvents hudEvents = HudEvents.getInstance();
        return (hudEvents.hasSelected(this.item) || this.itemIsHeld)
            || !this.config.getOnlyDisplayWhenHeld().get();
      } else {
        return false;
      }
    }
  }

  @Override
  public Icon createPlaceholderIcon() {
    ResourceLocation resourceLocation = ResourceLocation.create("cubepanion", "sprites.png");
    return Icon.sprite16(resourceLocation, this.posX, this.posY);
  }
}
