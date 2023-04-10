package org.cubecraftutilities.core.utils;

import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class VotingInterface {


  public void vote(CubeGame division, int left, int middle, int right) {
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
