package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import art.ameliah.laby.addons.cubepanion.core.events.PerkLoadEvent.PerkCategory;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class FunctionLink {

  protected List<Integer> perkSlots = List.of(20, 22, 24);
  protected List<Integer> perkCategorySlots = List.of(0, 1, 2);

  public abstract void setCoolDown(@NotNull ItemStack itemStack, int duration);

  public abstract CompletableFuture<Pair<PerkCategory, List<ItemStack>>> loadPerks();

}
