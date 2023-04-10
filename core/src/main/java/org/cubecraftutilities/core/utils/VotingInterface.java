package org.cubecraftutilities.core.utils;

import net.labymod.api.reference.annotation.Referenceable;
import net.labymod.api.util.concurrent.task.Task;
import org.cubecraftutilities.core.config.subconfig.AutoVoteSubConfig;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class VotingInterface {

  public void vote(CubeGame division, AutoVoteSubConfig autoVoteConfig) {
    switch (division) {
      case SOLO_SKYWARS:
        this.vote(
            division,
            autoVoteConfig.getSkyWarsChests().get().slot,
            autoVoteConfig.getSkyWarsProjectiles().get().slot,
            autoVoteConfig.getSkyWarsTime().get().slot
        );
        break;
      case SOLO_LUCKYISLANDS:
        this.vote(
            division,
            autoVoteConfig.getLuckyIslandsBlocks().get().slot,
            0,
            autoVoteConfig.getLuckyIslandsTime().get().slot
        );
        break;
      case TEAM_EGGWARS:
        this.vote(
            division,
            autoVoteConfig.getEggWarsItems().get().slot,
            0,
            autoVoteConfig.getEggWarsHealth().get().slot
        );
    }
  }
  private void vote(CubeGame division, int left, int middle, int right) {
    switch (division) {
      case TEAM_EGGWARS:
        this.voteEggWars(left, right);
        break;
      case SOLO_LUCKYISLANDS:
        this.voteLuckyIslands(left, right);
        break;
      case SOLO_SKYWARS:
        this.voteSkyWars(left, middle, right);
        break;
    }
  }

  public abstract void voteEggWars(int mode, int health);
  public abstract void voteSkyWars(int mode, int projectiles, int time);
  public abstract void voteLuckyIslands(int mode, int time);


}
