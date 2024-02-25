package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent.PerkCategory;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.labymod.api.Laby;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import net.labymod.api.util.Pair;
import org.jetbrains.annotations.NotNull;

public class PerkTracker {

  private final Cubepanion addon;
  private final @NotNull FunctionLink functionLink;

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

    CompletableFuture<Pair<PerkCategory, List<ItemStack>>> perks = functionLink.loadPerks();

    perks.whenComplete((pair, throwable) -> {
      if (throwable != null) {
        LOGGER.error(getClass(), throwable, "Failed to load perks");
        return;
      }
      if (pair == null || pair.getFirst() == null || pair.getSecond() == null) {
        return;
      }

      Laby.fireEvent(new PerkLoadEvent(pair.getFirst(), pair.getSecond()));
    }).exceptionally(throwable -> {
      LOGGER.error(getClass(), throwable, "Failed to load perks");
      return null;
    });
  }
}
