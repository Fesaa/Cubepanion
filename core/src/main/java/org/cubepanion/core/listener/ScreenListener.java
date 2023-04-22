package org.cubepanion.core.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenOpenEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.versionlinkers.LeaderboardTrackerLink;

public class ScreenListener {

  private final Cubepanion addon;
  private final LeaderboardTrackerLink leaderboardTrackerLink;

  public ScreenListener(Cubepanion addon, LeaderboardTrackerLink leaderboardTrackerLink) {
    this.addon = addon;
    this.leaderboardTrackerLink = leaderboardTrackerLink;
  }

  @Subscribe
  public void onOpenScreen(ScreenOpenEvent e) {
    if (this.addon.getManager().onCubeCraft()
        && this.addon.getManager().getDivision().equals(CubeGame.LOBBY)) {
      if (this.leaderboardTrackerLink != null
      && this.addon.configuration().getLeaderboardAPIConfig().getContributeToDB().get()) {
        this.leaderboardTrackerLink.onScreenOpen();
      }
    }
  }

}
