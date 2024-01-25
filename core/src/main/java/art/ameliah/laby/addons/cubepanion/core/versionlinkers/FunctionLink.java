package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class FunctionLink {

  public abstract void setCoolDown(@NotNull ItemStack itemStack, int duration);

}
