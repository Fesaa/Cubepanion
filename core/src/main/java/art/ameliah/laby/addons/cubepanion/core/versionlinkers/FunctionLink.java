package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class FunctionLink {

  private final Logging log = Logging.create(Cubepanion.class.getSimpleName());

  public abstract void setCoolDown(@NotNull ItemStack itemStack, int duration);

  public abstract void useItemInHotBar(int hotBarSlotIndex);

  public abstract void clickSlot(int slotId, int button);

  public CompletableFuture<@Nullable List<CCItemStack>> loadMenuItems(Predicate<String> titlePredicate) {
    return this.loadMenuItems(titlePredicate, null);
  }

  public CompletableFuture<@Nullable List<CCItemStack>> loadMenuItems(Predicate<String> titlePredicate, Predicate<List<CCItemStack>> itemPredicate) {
    return this.loadMenuContext(titlePredicate, itemPredicate).thenApplyAsync(ctx -> ctx == null ? null : ctx.items);
  }

  public abstract CompletableFuture<@Nullable MenuContext> loadMenuContext(Predicate<String> titlePredicate, Predicate<List<CCItemStack>> itemPredicate);

  protected  <T> CompletableFuture<T> try10Times(int tries, BooleanSupplier check, Supplier<T> res) {
    if (tries >= 10) {
      return CompletableFuture.completedFuture(null);
    }

    CompletableFuture<T> future = new CompletableFuture<>();

    Task.builder(() -> {
      if (check.getAsBoolean()) {
        future.complete(res.get());
        return;
      }

      try10Times(tries + 1, check, res).thenAcceptAsync(future::complete);
    }).delay(100, TimeUnit.MILLISECONDS).build().execute();

    return future;
  }

  public record MenuContext(String title, List<CCItemStack> items) {}

}
