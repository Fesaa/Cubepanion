package org.cubecraftutilities.core.listener.network;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.scoreboard.DisplaySlot;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.scoreboard.ScoreboardScore;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.scoreboard.ScoreboardObjectiveUpdateEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardScoreUpdateEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.managers.CCUManager;

public class ScoreboardListener {

  private final CCU addon;
  private final CCUManager manager;

  private boolean updatedMap;

  public ScoreboardListener(CCU addon) {
    this.addon = addon;
    this.manager = this.addon.getManager();
    this.updatedMap = false;
  }

  @Subscribe
  public void onScoreBoardScoreUpdate(ScoreboardScoreUpdateEvent e) {
    if (this.updatedMap) {
      return;
    }

    if (e.score().getName().contains("Map: ")) {
      if (this.manager.getDivisionName().equals("FFA")) {
        this.manager.setMapName(e.score().getName().substring(7));
      }
      return;
    }
    Scoreboard scoreboard = this.addon.labyAPI().minecraft().getScoreboard();
    if (scoreboard == null) {
      return;
    }
    ScoreboardScore lastEntry = null;
    for (ScoreboardScore score : scoreboard.getScores(scoreboard.getObjective(DisplaySlot.SIDEBAR))) {
      if (score.getName().contains("Map:")) {
        if (lastEntry != null) {
          this.manager.setMapName(lastEntry.getName().substring(2));
        }
        this.addon.rpcManager.updateRPC();
        return;
      }
      lastEntry = score;
    }
    this.manager.setMapName("");
    this.addon.rpcManager.updateRPC();
    this.updatedMap = true;
  }

  @Subscribe
  public void onScoreboardObjectiveUpdate(ScoreboardObjectiveUpdateEvent e) {
    if (!e.objective().getName().equals("sidebar")) {
      return;
    }
    if (this.manager.hasUpdatedAfterServerSwitch()) {
      return;
    }

    Component title = e.objective().getTitle();
    String titleText = ((TextComponent) title).getText();
    if (titleText != null && titleText.matches("[a-zA-Z ]*")) {
      this.manager.setDivisionName(titleText);
    } else {
      for (Component child : title.getChildren()) {
        String text = ((TextComponent) child).getText();
        if (text == null) {
          continue;
        }
        if (text.matches("[a-zA-Z ]*")) {
          this.manager.setDivisionName(text);
          break;
        }
      }
    }
    this.manager.setHasUpdatedAfterServerSwitch(true);
    this.updatedMap = false;
  }

}
