package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import art.ameliah.laby.addons.cubepanion.core.utils.AutoVoteProvider;
import java.util.function.Supplier;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class VotingLink {

  public final int returnIndex = 31;

  public abstract void openMenu(int hotbarSlotIndex);

  public abstract void clickSlot(int syncId, int slotId, int button);

  public void vote(AutoVoteProvider provider) {
    this.openMenu(provider.getHotbarSlot());

    int syncId = 1;
    for (Supplier<VotePair> votePairSupplier : provider.getVotePairSuppliers()) {
      VotePair pair = votePairSupplier.get();
      if (!pair.valid()) {
        continue;
      }

      if (pair.choiceIndex() != -1) {
        this.clickSlot(syncId, pair.choiceIndex(), 0);
        syncId++;
      }

      this.clickSlot(syncId, pair.voteIndex(), 0);

      if (pair.choiceIndex() != -1) {
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
