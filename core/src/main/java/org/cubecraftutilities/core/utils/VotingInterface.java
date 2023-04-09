package org.cubecraftutilities.core.utils;

import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Nullable
@Referenceable
public abstract class VotingInterface {


  public void vote(String division, int left, int middle, int right) {
    switch (division) {
      case "Team EggWars":
        this.voteEggWars(left, right);
        break;
    }
  };

  public abstract void voteEggWars(int mode, int health);


}
