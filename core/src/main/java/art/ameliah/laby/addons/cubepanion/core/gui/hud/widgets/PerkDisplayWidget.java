package art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets;

import art.ameliah.laby.addons.cubepanion.core.events.GameEndEvent;
import art.ameliah.laby.addons.cubepanion.core.events.GameUpdateEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent.PerkCategory;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.PerkDisplayWidget.PerkConfig;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.base.ItemDisplayConfig;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.base.ItemDisplayWidget;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.Orientation;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import org.jetbrains.annotations.NotNull;

public class PerkDisplayWidget extends ItemDisplayWidget<PerkConfig> {
  private final PerkCategory category;

  public PerkDisplayWidget(HudWidgetCategory hudWidgetCategory, PerkCategory category) {
    super("perk_tracking_" + category.name().toLowerCase(), hudWidgetCategory, PerkConfig.class);

    this.category = category;

    ItemStack stone = Laby.references().itemStackFactory()
        .create(ResourceLocation.create("minecraft", "stone"));
    ItemStack dirt = Laby.references().itemStackFactory()
        .create(ResourceLocation.create("minecraft", "dirt"));

    setDummyItems(List.of(new PerkItem(stone), new PerkItem(dirt)));
  }

  @Subscribe
  public void updateItems(PerkLoadEvent e) {
    if (e.getCategory() != category) {
      return;
    }

    List<DisplayItem> perks = new ArrayList<>();
    for (ItemStack perk : e.getPerks()) {
      perks.add(new PerkItem(perk));
    }

    setItems(perks);
  }

  @Subscribe
  public void clearItems(GameUpdateEvent e) {
    clearItems();
  }

  @Subscribe
  public void clearItems(GameEndEvent e) {
    clearItems();
  }

  @Override
  protected boolean shouldRenderName() {
    return config.getOrientation().get().equals(Orientation.VERTICAL);
  }

  public static class PerkConfig extends ItemDisplayConfig {

    public PerkConfig() {
      getShowName().customRequires((b) -> getOrientation().get().equals(Orientation.VERTICAL));
    }
  }

  public static class PerkItem implements DisplayItem {

    private final ItemStack itemStack;

    public PerkItem(ItemStack itemStack) {
      this.itemStack = itemStack;
    }

    @Override
    public @NotNull RenderableComponent getRenderableComponent() {
      return RenderableComponent.of(itemStack.getDisplayName());
    }

    @Override
    public @NotNull Component getComponent() {
      return itemStack.getDisplayName();
    }

    @Override
    public @NotNull ItemStack backingItemStack() {
      return itemStack;
    }
  }
}
