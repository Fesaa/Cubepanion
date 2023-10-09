package org.cubepanion.core.listener.network;

import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.scoreboard.ScoreboardObjectiveUpdateEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamEntryAddEvent;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.managers.CubepanionManager;
import org.cubepanion.core.managers.DiscordAPI;
import org.cubepanion.core.utils.CubeGame;

public class ScoreboardListener {

  private final Cubepanion addon;
  private final CubepanionManager manager;

  private String previousText;
  private boolean updatedMap;

  public ScoreboardListener(Cubepanion addon) {
    this.addon = addon;
    this.manager = this.addon.getManager();

    this.previousText = "";
    this.updatedMap = false;
  }

  @Subscribe
  public void onScoreboardTeamEntryAddEvent(ScoreboardTeamEntryAddEvent e) {
    if (this.updatedMap || !this.addon.getManager().onCubeCraft()) {
      return;
    }

    List<Component> children = e.team().getPrefix().getChildren();
    if (!children.isEmpty()) {
      if (this.manager.getDivision().equals(CubeGame.FFA)) {
        List<Component> ffaComponent = children.get(0).getChildren();
        if (ffaComponent.size() == 2) {
          if (((TextComponent) ffaComponent.get(0)).getText().contains("Map: ")) {
            this.manager.setMapName(((TextComponent) ffaComponent.get(1)).getText());
            this.updatedMap = true;
          }
        }
      } else if (this.manager.getDivision().equals(CubeGame.LOBBY)
          && this.manager.hasUpdatedAfterServerSwitch()) {
        this.manager.setMapName("Main Lobby");
        this.updatedMap = true;
      } else {
        String text = ((TextComponent) children.get(0)).getText();
        if (this.previousText.equals("Map:") || this.previousText.equals("Dimension:")) {
          this.manager.setMapName(text);
          this.updatedMap = true;
        }
        this.previousText = text;
      }
    }
    if (this.updatedMap) {
      DiscordAPI.getInstance().updateRPC();
    }
  }

  @Subscribe
  public void onScoreboardObjectiveUpdate(ScoreboardObjectiveUpdateEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }
    if (!e.objective().getName().equals("sidebar")) {
      return;
    }
    if (this.manager.hasUpdatedAfterServerSwitch() && !CubeGame.isParkour(
        this.manager.getDivision())) {
      return;
    }

    Component title = e.objective().getTitle();
    String titleText = ((TextComponent) title).getText();
    if (titleText != null && titleText.matches("[a-zA-Z ]*") && !titleText.isEmpty()) {
      this.manager.setDivision(CubeGame.stringToGame(titleText.trim()));
      this.manager.setHasUpdatedAfterServerSwitch(true);
    } else {
      for (Component child : title.getChildren()) {
        String text = ((TextComponent) child).getText();
        if (text == null) {
          continue;
        }
        text = text.replaceAll("[^a-zA-Z \\.]", "").trim();
        if (text.matches("[a-zA-Z ]+")) {
          this.manager.setDivision(CubeGame.stringToGame(text.trim()));
          this.manager.setHasUpdatedAfterServerSwitch(true);
          break;
        }
      }
    }

    this.updatedMap = false;
  }

}
