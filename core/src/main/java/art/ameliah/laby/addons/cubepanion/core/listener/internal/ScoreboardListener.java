package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent.RequestType;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardObjectiveUpdateEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamEntryAddEvent;

public class ScoreboardListener {

  private final Cubepanion addon;
  private final CubepanionManager manager;

  private String previousText;
  private boolean updatedMap;
  private boolean updatedDivision;

  public ScoreboardListener(Cubepanion addon) {
    this.addon = addon;
    this.manager = this.addon.getManager();

    this.previousText = "";
    this.updatedMap = false;
  }

  @Subscribe
  public void onServerSwitch(SubServerSwitchEvent e) {
    updatedDivision = false;
  }

  @Subscribe
  public void onScoreboardTeamEntryAddEvent(ScoreboardTeamEntryAddEvent e) {
    if (this.updatedMap || !this.addon.getManager().onCubeCraft()) {
      return;
    }

    List<Component> children = e.team().getPrefix().getChildren();
    if (children.isEmpty()) {
      return;
    }

    switch (manager.getDivision()) {
      case FFA -> {
        List<Component> ffaComponent = children.get(0).getChildren();
        if (ffaComponent.size() == 2) {
          if (((TextComponent) ffaComponent.get(0)).getText().contains("Map: ")) {
            this.manager.setMapName(((TextComponent) ffaComponent.get(1)).getText());
            this.updatedMap = true;
          }
        }
      }
      case LOBBY -> {
        if (updatedDivision) {
          this.manager.setMapName("Main Lobby");
          this.updatedMap = true;
        }
      }
      default -> {
        String text = ((TextComponent) children.get(0)).getText();
        if (this.previousText.equals("Map:") || this.previousText.equals("Dimension:")) {
          this.manager.setMapName(text);
          this.updatedMap = true;
        }
        this.previousText = text;
      }
    }
    if (this.updatedMap) {
      Laby.fireEvent(new RequestEvent(RequestType.UPDATE_RPC));
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
    if (updatedDivision && !CubeGame.isParkour(this.manager.getDivision())) {
      return;
    }

    Component title = e.objective().getTitle();
    String titleText = ((TextComponent) title).getText();
    if (titleText != null && !titleText.isEmpty() && titleText.matches("[a-zA-Z ]*")) {
      this.manager.setDivision(CubeGame.stringToGame(titleText.trim()));
      updatedDivision = true;
    } else {
      for (Component child : title.getChildren()) {
        String text = ((TextComponent) child).getText();
        if (text == null) {
          continue;
        }
        text = text.replaceAll("[^a-zA-Z \\.]", "").trim();
        if (text.matches("[a-zA-Z ]+")) {
          this.manager.setDivision(CubeGame.stringToGame(text.trim()));
          updatedDivision = true;
          break;
        }
      }
    }

    this.updatedMap = false;
  }

}
