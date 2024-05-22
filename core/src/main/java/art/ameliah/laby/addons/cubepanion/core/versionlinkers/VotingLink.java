package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;
import art.ameliah.laby.addons.cubepanion.core.utils.AutoVoteProvider;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class VotingLink {

  public final int returnIndex = 31;

  protected int hotbarSlotIndex;

  protected Deque<VotePair> deque = new ArrayDeque<>();

  private void addToDeque(int slot, int voteIndex) {
    if (voteIndex != -1) {
      deque.add(new VotePair(slot, voteIndex));
    }
  }

  public @NotNull VotePair getNextVotePair() {
    if (deque.isEmpty()) {
      return new VotePair(-1, -1);
    }
    return deque.poll();
  }

  public void vote(AutoVoteProvider provider) {
    hotbarSlotIndex = provider.getHotbarSlot();
    boolean vote = false;
    deque.clear();
    for (Supplier<VotePair> supplier : provider.getVotePairSuppliers()) {
      VotePair pair = supplier.get();
      deque.add(pair);
      vote |= pair.voteIndex != -1;
    }
    if (vote) {
      this.startAutoVote();
    } else {
      deque.clear();
    }
  }

  public abstract void startAutoVote();

  public record VotePair(int choiceIndex, int voteIndex) {

    public static VotePair of(int choiceIndex, int voteIndex) {
      return new VotePair(choiceIndex, voteIndex);
    }
  }


}
