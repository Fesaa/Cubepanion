package art.ameliah.laby.addons.cubepanion.core.versionlinkers;

import art.ameliah.laby.addons.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import java.util.ArrayDeque;
import java.util.Deque;
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

  public void vote(CubeGame division, AutoVoteSubConfig autoVoteConfig) {
    switch (division) {
      case SOLO_SKYWARS -> this.voteSkyWars(
          autoVoteConfig.getSkyWarsChests().get().slot,
          autoVoteConfig.getSkyWarsProjectiles().get().slot,
          autoVoteConfig.getSkyWarsTime().get().slot
      );
      case SOLO_LUCKYISLANDS -> this.voteLuckyIslands(
          autoVoteConfig.getLuckyIslandsBlocks().get().slot,
          autoVoteConfig.getLuckyIslandsTime().get().slot
      );
      case TEAM_EGGWARS -> this.voteEggWars(
          autoVoteConfig.getEggWarsItems().get().slot,
          autoVoteConfig.getEggWarsHealth().get().slot,
          autoVoteConfig.getEggWarsPerk().get().slot
      );
      case PILLARS_OF_FORTUNE -> this.votePof(
          autoVoteConfig.getPofGameMode().get().slot,
          autoVoteConfig.getPofMapMode().get().slot
      );
    }
  }

  public void votePof(int gameMode, int mapMode) {
    if (gameMode == -1 && mapMode == -1) {
      return;
    }
    hotbarSlotIndex = 0;
    deque.clear();
    addToDeque(12, gameMode);
    addToDeque(14, mapMode);
    this.startAutoVote();
  }

  public void voteEggWars(int mode, int health, int perk) {
    if (mode == -1 && health == -1 && perk == -1) {
      return;
    }
    hotbarSlotIndex = 2;
    deque.clear();
    addToDeque(11, perk);
    addToDeque(13, mode);
    addToDeque(15, health);
    this.startAutoVote();
  }

  public void voteSkyWars(int mode, int projectiles, int time) {
    if (mode == -1 && projectiles == -1 && time == -1) {
      return;
    }
    hotbarSlotIndex = 1;
    deque.clear();
    addToDeque(11, mode);
    addToDeque(13, projectiles);
    addToDeque(15, time);
    this.startAutoVote();
  }

  public void voteLuckyIslands(int mode, int time) {
    if (mode == -1 && time == -1) {
      return;
    }
    hotbarSlotIndex = 1;
    deque.clear();
    addToDeque(12, mode);
    addToDeque(14, time);

    this.startAutoVote();
  }

  public abstract void startAutoVote();

  public record VotePair(int choiceIndex, int voteIndex) {

  }


}
