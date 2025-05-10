package art.ameliah.laby.addons.cubepanion.core.listener.hud;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.entity.player.Inventory;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.client.world.item.Item;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import net.labymod.api.util.Pair;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;

public class HudEvents {

  private static final Logging log = Logging.create(Cubepanion.class.getSimpleName());

  private static final ItemStack AIR = Laby.references().itemStackFactory()
      .create(ResourceLocation.create("minecraft", "air"));
  private static HudEvents hudEvents;
  private final Cubepanion addon;

  private final Map<Item, Integer> trackedItems;
  private final Map<String, Integer> trackedRegexes;
  private final Map<String, ItemStack> lastRegexMatches;
  private ItemStack selectedStack;

  public HudEvents(Cubepanion addon) {
    hudEvents = this;

    this.addon = addon;
    this.trackedItems = new HashMap<>();
    this.trackedRegexes = new HashMap<>();
    this.lastRegexMatches = new HashMap<>();
  }

  // This will never be null
  public static @NotNull HudEvents getInstance() {
    return hudEvents;
  }

  public IntSupplier registerItemStack(ItemStack itemStack) {
    this.trackedItems.put(itemStack.getAsItem(), 0);
    return () -> this.trackedItems.get(itemStack.getAsItem());
  }

  public Pair<IntSupplier, Supplier<ItemStack>> registerRegex(String regex) {
    this.trackedRegexes.put(regex, 0);
    return Pair.of(
        () -> this.trackedRegexes.get(regex),
        () -> this.lastRegexMatches.get(regex)
    );
  }

  public boolean hasSelected(ItemStack itemStack) {
    if (this.selectedStack == null || itemStack == null) {
      return false;
    }
    return this.selectedStack.matches(itemStack);
  }

  @Subscribe
  public void onGameTickEvent(GameTickEvent e) {
    if (e.phase() == Phase.POST) {
      return;
    }

    ClientPlayer player = this.addon.labyAPI().minecraft().getClientPlayer();
    if (player == null) {
      return;
    }
    this.resetMaps();

    ItemStack offHandItem = player.getOffHandItemStack();
    this.updateMaps(offHandItem);

    Inventory inventory = player.inventory();
    for (int i = 0; i < 46; i++) {
      ItemStack itemStack = inventory.itemStackAt(i);
      this.updateMaps(itemStack);
      if (i == inventory.getSelectedIndex()) {
        this.selectedStack = itemStack;
      }
    }

  }

  private void resetMaps() {
    this.trackedRegexes.forEach((k, v) -> this.trackedRegexes.put(k, 0));
    this.trackedItems.forEach((k, v) -> this.trackedItems.put(k, 0));
    this.lastRegexMatches.forEach((k, v) -> this.lastRegexMatches.put(k, AIR));
  }

  private void updateMaps(ItemStack itemStack) {
    if (itemStack == null) {
      log.debug("null itemstack passed to updateMaps");
      return;
    }
    Item item = itemStack.getAsItem();
    this.trackedItems.computeIfPresent(item, (k, v) -> v + itemStack.getSize());
    this.trackedRegexes.keySet().forEach((k) -> {
      if (item.getIdentifier().getPath().matches(k)) {
        this.trackedRegexes.compute(k, (k2, v) -> {
          if (v == null) {
            v = 0;
          }
          this.lastRegexMatches.put(k, itemStack);
          return v + itemStack.getSize();
        });
      }
    });
  }
}
