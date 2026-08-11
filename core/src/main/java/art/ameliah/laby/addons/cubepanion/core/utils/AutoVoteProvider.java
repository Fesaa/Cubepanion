package art.ameliah.laby.addons.cubepanion.core.utils;

import art.ameliah.laby.addons.cubepanion.core.listener.games.AutoVote.VotePair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AutoVoteProvider {

  private final int hotbarSlot;
  private final List<Supplier<VotePair>> votePairSuppliers = new ArrayList<>();

  private AutoVoteProvider(int hotbarSlot, List<Supplier<VotePair>> votePairSuppliers) {
    this.votePairSuppliers.addAll(votePairSuppliers);
    this.hotbarSlot = hotbarSlot;
  }

  @SafeVarargs
  public static AutoVoteProvider of(int hotbarSlot, Supplier<VotePair>... supplier) {
    return new AutoVoteProvider(hotbarSlot, List.of(supplier));
  }

  public static AutoVoteProvider of(int hotbarSlot, List<Supplier<VotePair>> suppliers) {
    return new AutoVoteProvider(hotbarSlot, suppliers);
  }

  public int getHotbarSlot() {
    return hotbarSlot;
  }

  public List<Supplier<VotePair>> getVotePairSuppliers() {
    return votePairSuppliers;
  }

}
