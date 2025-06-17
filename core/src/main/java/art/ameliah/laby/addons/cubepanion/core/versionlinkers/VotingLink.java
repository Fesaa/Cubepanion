package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.utils.AutoVoteProvider;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import net.labymod.api.Laby;
import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class VotingLink {

  protected static final Logging log = Logging.create(Cubepanion.class.getSimpleName());

  public final int returnIndex = 31;

  public abstract void openMenu(int hotbarSlotIndex);

  public abstract void clickSlot(int slotId, int button);

  public record VotePair(int choiceIndex, int voteIndex, String menuTitle) {

    public static VotePair of(int choiceIndex, int voteIndex, String menuTitle) {
      return new VotePair(choiceIndex, voteIndex, menuTitle.toLowerCase());
    }

    public boolean valid() {
      return voteIndex >= 0;
    }

  }


}
