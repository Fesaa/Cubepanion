package org.cubepanion.core.utils;

import net.labymod.api.reference.annotation.Referenceable;
import org.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class VotingInterface {

  public final int returnIndex = 31;

  protected int hotbarSlotIndex;
  protected int leftChoiceIndex;
  protected int leftVoteIndex;
  protected int middleChoiceIndex;
  protected int middleVoteIndex;
  protected int rightChoiceIndex;
  protected int rightVoteIndex;

  public int getNextChoiceIndex() {
    int temp;
    if (this.leftChoiceIndex != -1) {
      temp = this.leftChoiceIndex;
      this.leftChoiceIndex = -1;
    } else if (this.middleChoiceIndex != -1) {
      temp = this.middleChoiceIndex;
      this.middleChoiceIndex = -1;
    } else {
      temp = this.rightChoiceIndex;
      this.rightChoiceIndex = -1;
    }
    return temp;
  }

  public int getNextVoteIndex() {
    int temp;
    if (this.leftVoteIndex != -1) {
      temp = this.leftVoteIndex;
      this.leftVoteIndex = -1;
    } else if (this.middleVoteIndex != -1) {
      temp = this.middleVoteIndex;
      this.middleVoteIndex = -1;
    } else {
      temp = this.rightVoteIndex;
      this.rightVoteIndex = -1;
    }
    return temp;
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


}
