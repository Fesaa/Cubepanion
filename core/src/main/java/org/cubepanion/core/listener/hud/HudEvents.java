package org.cubepanion.core.listener.hud;

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
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.LOGGER;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class HudEvents {

  private static HudEvents hudEvents;

  // This will never be null
  public static @NotNull HudEvents getInstance() {
    return hudEvents;
  }

  private final Cubepanion addon;

  private final Map<Item, Integer> trackedItems;
  private final Map<String, Integer> trackedRegexes;
  private final Map<String, ItemStack> lastRegexMatches;
  private static final ItemStack AIR = Laby.references().itemStackFactory()
      .create(ResourceLocation.create("minecraft", "air"));

  private ItemStack selectedStack;

  public HudEvents(Cubepanion addon) {
    hudEvents = this;

    this.addon = addon;
    this.trackedItems = new HashMap<>();
    this.trackedRegexes = new HashMap<>();
    this.lastRegexMatches = new HashMap<>();
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
    if (this.selectedStack == null) {
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
      LOGGER.debug(getClass(), "Got a null ItemStack in HudEvents#updateMaps");
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
