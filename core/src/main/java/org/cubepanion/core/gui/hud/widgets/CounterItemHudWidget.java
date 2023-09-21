package org.cubepanion.core.gui.hud.widgets;

import java.util.function.IntSupplier;
import java.util.function.Supplier;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.ItemStack;
import org.cubepanion.core.gui.hud.widgets.base.CustomItemWidget;
import org.cubepanion.core.listener.hud.HudEvents;
import org.jetbrains.annotations.Nullable;

public class CounterItemHudWidget extends CustomItemWidget {

  private final IntSupplier supplier;
  private final @Nullable Supplier<ItemStack> itemStackSupplier;

  public CounterItemHudWidget(HudWidgetCategory category, String id, String regex, String itemName,
      int posX, int posY) {
    super(id, regex, itemName, posX, posY);

    this.bindCategory(category);
    var pair = HudEvents.getInstance().registerRegex(regex);
    this.supplier = pair.getFirst();
    this.itemStackSupplier = pair.getSecond();
  }

  public CounterItemHudWidget(HudWidgetCategory category, String id, String itemName,
      int posX, int posY) {
    super(id, "", itemName, posX, posY);

    this.bindCategory(category);
    ItemStack item = Laby.references().itemStackFactory()
        .create(ResourceLocation.create("minecraft", itemName));
    this.supplier = HudEvents.getInstance().registerItemStack(item);
    this.itemStackSupplier = null;
  }

  @Override
  public void onTick(boolean inEditor) {
    if (inEditor) {
      this.updateItemName(Component.text("1", this.config.getTextColor()), true);
      return;
    }
    this.counter = this.supplier.getAsInt();
    if (this.itemStackSupplier != null) {
      this.updateItemStack(this.itemStackSupplier.get(), false);
    }
    this.updateItemName(Component.text(this.counter, this.config.getTextColor()), false);
  }

}
