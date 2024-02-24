package art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets;

import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent.PerkCategory;
import art.ameliah.laby.addons.cubepanion.core.gui.hud.widgets.base.ItemDisplayWidget;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.hud.binding.category.HudWidgetCategory;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import org.jetbrains.annotations.NotNull;

public class PerkDisplayWidget extends ItemDisplayWidget {
  private final PerkCategory category;

  public PerkDisplayWidget(HudWidgetCategory hudWidgetCategory, PerkCategory category) {
    super("cubepanion_perk_tracking_" + category.name(), hudWidgetCategory);

    this.category = category;
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
