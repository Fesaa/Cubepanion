package org.cubepanion.core.listener;

import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.gui.screen.ScreenDisplayEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.versionlinkers.LeaderboardTrackerLink;
import org.cubepanion.core.versionlinkers.QOLMapSelectorLink;

public class ScreenListener {

  private final Cubepanion addon;
  private final LeaderboardTrackerLink leaderboardTrackerLink;
  private final QOLMapSelectorLink qolMapSelectorLink;

  public ScreenListener(Cubepanion addon, LeaderboardTrackerLink leaderboardTrackerLink,
      QOLMapSelectorLink qolMapSelectorLink) {
    this.addon = addon;
    this.leaderboardTrackerLink = leaderboardTrackerLink;
    this.qolMapSelectorLink = qolMapSelectorLink;
  }

  @Subscribe
  public void onDisplayScreen(ScreenDisplayEvent e) {
    if (this.addon.getManager().onCubeCraft()
        && this.addon.getManager().getDivision().equals(CubeGame.LOBBY)) {
      if (this.leaderboardTrackerLink != null
          && this.addon.configuration().getLeaderboardAPIConfig().getContributeToDB().get()) {
        this.leaderboardTrackerLink.onScreenOpen();
      }
      if (this.qolMapSelectorLink != null
          && this.addon.configuration().getQolConfig().getMapSelector().get()) {
        this.qolMapSelectorLink.onScreenOpen();
      }
    }
  }

}
