package org.cubepanion.core.versionlinkers;

import net.labymod.api.reference.annotation.Referenceable;
import org.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import org.cubepanion.core.utils.CubeGame;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class VotingLink {

  public final int returnIndex = 31;

  protected int hotbarSlotIndex;
  protected int leftChoiceIndex;
  protected int leftVoteIndex;
  protected int middleChoiceIndex;
  protected int middleVoteIndex;
  protected int rightChoiceIndex;
  protected int rightVoteIndex;

  public VotePair getNextVotePair() {
    int voteIndex;
    int choiceIndex;
    if (this.leftVoteIndex != -1) {
      voteIndex = this.leftVoteIndex;
      this.leftVoteIndex = -1;
      if (this.leftChoiceIndex != -1) {
        choiceIndex = this.leftChoiceIndex;
        this.leftChoiceIndex = -1;
        return new VotePair(choiceIndex, voteIndex);
      }
    }

    if (this.middleVoteIndex != -1) {
      voteIndex = this.middleVoteIndex;
      this.middleVoteIndex = -1;
      if (this.middleChoiceIndex != -1) {
        choiceIndex = this.middleChoiceIndex;
        this.middleChoiceIndex = -1;
        return new VotePair(choiceIndex, voteIndex);
      }

    }
    if (this.rightVoteIndex != -1) {
      voteIndex = this.rightVoteIndex;
      this.rightVoteIndex = -1;
      if (this.rightChoiceIndex != -1) {
        choiceIndex = this.rightChoiceIndex;
        this.rightChoiceIndex = -1;
        return new VotePair(choiceIndex, voteIndex);
      }
    }

    return new VotePair(-1, -1);
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
      case TEAM_EGGWARS -> this.voteEggWars(autoVoteConfig.getEggWarsItems().get().slot,
          autoVoteConfig.getEggWarsHealth().get().slot
      );
    }
  }

  public void voteEggWars(int mode, int health) {
    if (mode == -1 && health == -1) {
      return;
    }
    this.hotbarSlotIndex = 2;
    this.leftChoiceIndex = 12;
    this.leftVoteIndex = mode;
    this.middleChoiceIndex = -1;
    this.middleVoteIndex = -1;
    this.rightChoiceIndex = 14;
    this.rightVoteIndex = health;

    this.startAutoVote();
  }

  public void voteSkyWars(int mode, int projectiles, int time) {
    if (mode == -1 && projectiles == -1 && time == -1) {
      return;
    }
    this.hotbarSlotIndex = 1;
    this.leftChoiceIndex = 11;
    this.leftVoteIndex = mode;
    this.middleChoiceIndex = 13;
    this.middleVoteIndex = projectiles;
    this.rightChoiceIndex = 15;
    this.rightVoteIndex = time;

    this.startAutoVote();
  }

  public void voteLuckyIslands(int mode, int time) {
    if (mode == -1 && time == -1) {
      return;
    }
    this.hotbarSlotIndex = 1;
    this.leftChoiceIndex = 12;
    this.leftVoteIndex = mode;
    this.middleChoiceIndex = -1;
    this.middleVoteIndex = -1;
    this.rightChoiceIndex = 14;
    this.rightVoteIndex = time;

    this.startAutoVote();
  }

  public abstract void startAutoVote();

  public record VotePair(int choiceIndex, int voteIndex) {};


}
