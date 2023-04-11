package org.cubecraftutilities.core.listener.network;

import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.scoreboard.ScoreboardObjectiveUpdateEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamEntryAddEvent;
import org.cubecraftutilities.core.CCU;
import org.cubecraftutilities.core.managers.CCUManager;
import org.cubecraftutilities.core.utils.CubeGame;

public class ScoreboardListener {

  private final CCU addon;
  private final CCUManager manager;

  private String previousText;
  private boolean updatedMap;

  public ScoreboardListener(CCU addon) {
    this.addon = addon;
    this.manager = this.addon.getManager();

    this.previousText = "";
    this.updatedMap = false;
  }

  @Subscribe
  public void onScoreboardTeamEntryAddEvent(ScoreboardTeamEntryAddEvent e) {
    if (this.updatedMap) {
      return;
    }

    List<Component> children = e.team().getPrefix().getChildren();
    if (children.size() > 0) {
      if (this.manager.getDivision().equals(CubeGame.FFA)) {
        List<Component> ffaComponent = children.get(0).getChildren();
        if (ffaComponent.size() == 2) {
          if (((TextComponent) ffaComponent.get(0)).getText().contains("Map: ")) {
            this.manager.setMapName(((TextComponent) ffaComponent.get(1)).getText());
            this.updatedMap = true;
          }
        }
      } else if (this.manager.getDivision().equals(CubeGame.LOBBY)) {
        this.manager.setMapName("Main Lobby");
        this.updatedMap = true;
      } else {
        String text = ((TextComponent) children.get(0)).getText();
        if (this.previousText.equals("Map:")) {
          this.manager.setMapName(text);
          this.updatedMap = true;
        }
        this.previousText = text;
      }
    }
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
    if (titleText != null && titleText.matches("[a-zA-Z ]*") && titleText.length() > 0) {
      this.manager.setDivision(CubeGame.stringToGame(titleText.trim()));
    } else {
      for (Component child : title.getChildren()) {
        String text = ((TextComponent) child).getText();
        if (text == null) {
          continue;
        }
        if (text.matches("[a-zA-Z ]+")) {
          this.manager.setDivision(CubeGame.stringToGame(text.trim()));
          break;
        }
      }
    }
    this.manager.setHasUpdatedAfterServerSwitch(true);
    this.updatedMap = false;
  }

}
