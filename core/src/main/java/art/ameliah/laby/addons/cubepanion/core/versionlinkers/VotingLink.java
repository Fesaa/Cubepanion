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

  public abstract void clickSlot(int syncId, int slotId, int button);

  public void vote(AutoVoteProvider provider) {
    log.debug("Starting vote sequence for hotbar slot {}", provider.getHotbarSlot());
    Task.builder(() -> {
      this.openMenu(provider.getHotbarSlot());
      Laby.labyAPI().minecraft().executeNextTick(() -> this.menuLogic(provider));
    }).delay(100, TimeUnit.MILLISECONDS).build().execute();
  }

  private void menuLogic(AutoVoteProvider provider) {
    int syncId = 1;
    for (Supplier<VotePair> votePairSupplier : provider.getVotePairSuppliers()) {
      VotePair pair = votePairSupplier.get();
      if (!pair.valid()) {
        continue;
      }

      if (pair.choiceIndex() != -1) {
        log.debug("Clicking choice {} w/ syncId {}", pair.choiceIndex(), syncId);
        this.clickSlot(syncId, pair.choiceIndex(), 0);
        syncId++;
      }

      log.debug("Clicking vote {} w/ syncId {}", pair.choiceIndex(), syncId);
      this.clickSlot(syncId, pair.voteIndex(), 0);

      if (pair.choiceIndex() != -1) {
        log.debug("Clicking choice {} w/ syncId {}", pair.choiceIndex(), syncId);
        this.clickSlot(syncId, returnIndex, 0);
        syncId++;
      }
    }
    this.clickSlot(syncId, returnIndex, 0);
  }

  public record VotePair(int choiceIndex, int voteIndex) {

    public static VotePair of(int choiceIndex, int voteIndex) {
      return new VotePair(choiceIndex, voteIndex);
    }

    public boolean valid() {
      return voteIndex >= 0;
    }

  }


}
