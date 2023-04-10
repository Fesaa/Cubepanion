package org.cubecraftutilities.core.listener;

import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.utils.VotingInterface;

public class VotingTests {

  private final VotingInterface votingInterface;
  private final CCU addon;

  public VotingTests(VotingInterface votingInterface, CCU addon) {
    this.votingInterface = votingInterface;
    this.addon = addon;
  }

  @Subscribe
  public void onKeyEvent(KeyEvent e) {

    if (e.key().equals(Key.L) && e.state() == State.PRESS) {
      this.votingInterface.vote(
          this.addon.getManager().getDivision(),
          this.addon.configuration().getAutoVoteSubConfig());
    }

  }

}
