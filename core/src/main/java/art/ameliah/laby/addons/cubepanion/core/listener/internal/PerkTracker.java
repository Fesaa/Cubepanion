package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent.PerkCategory;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.util.Pair;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;

public class PerkTracker {

  private final Logging log = Logging.create(Cubepanion.class.getSimpleName());

  private final Cubepanion addon;
  private final @NotNull FunctionLink functionLink;

  protected List<Integer> perkSlots = List.of(20, 22, 24);
  protected List<Integer> perkCategorySlots = List.of(0, 1, 2);

  public PerkTracker(Cubepanion addon, @NotNull FunctionLink functionLink) {
    this.addon = addon;
    this.functionLink = functionLink;
  }

  @Subscribe
  public void onScreenDisplay(ScreenDisplayEvent e) {
    if (!addon.getManager().onCubeCraft()) {
      return;
    }

    if (!addon.getManager().isPlaying(CubeGame.TEAM_EGGWARS)) {
      return;
    }

    functionLink.loadMenuItems(title -> title.contains("Perk Shop"))
        .thenApplyAsync(this::extractPerks)
        .whenComplete(this::fireEventIfValid)
        .exceptionally(throwable -> {
          log.error("failed to load perks {}", throwable);
          return null;
        });
  }

  private void fireEventIfValid(Pair<PerkCategory, List<ItemStack>> pair, Throwable throwable) {
    if (throwable != null) {
      log.error("failed to load perks", throwable);
      return;
    }
    if (pair == null || pair.getFirst() == null || pair.getSecond() == null) {
      return;
    }

    Laby.fireEvent(new PerkLoadEvent(pair.getFirst(), pair.getSecond()));
  }

  private Pair<PerkCategory, List<ItemStack>> extractPerks(List<CCItemStack> items) {
    if (items == null || items.isEmpty()) {
      return null;
    }

    PerkCategory category = this.getPerkCategory(items);
    if (category == null) {
      log.debug("No PerkCategory found");
      return null;
    }

    List<ItemStack> perks = new ArrayList<>();
    for (int i : perkSlots) {
      var item = items.get(i);
      if (!item.isAir()) {
        perks.add(item);
      }
    }

    return Pair.of(category, perks);
  }

  private PerkCategory getPerkCategory(List<CCItemStack> items) {
    for (int i : perkCategorySlots) {
      var item = items.get(i);

      if (item.getAsItem().isAir()) {
        continue;
      }

      var itemVar = item.getCustomDataTag().getString("cubetap:item_variant");
      if (itemVar.isEmpty()) {
        continue;
      }

      String var = itemVar.get();
      if (!var.endsWith("_selected")) {
        continue;
      }

      PerkCategory category = PerkCategory.fromCubeTapItemVariant(var.replace("_selected", ""));
      if (category != null) {
        return category;
      }
    }

    return null;
  }


}
