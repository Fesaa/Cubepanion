package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.GameStartEvent;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent.RequestType;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardObjectiveUpdateEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamEntryAddEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamUpdateEvent;
import net.labymod.api.util.logging.Logging;

public class ScoreboardListener {

  private static final Pattern DATE_SERVER_ID_REGEX = Pattern.compile(
      "[0-9]{2}/[0-9]{2}/[0-9]{2} \\((.{5})\\)");

  private final Logging log = Logging.create(Cubepanion.class.getSimpleName());

  private final Cubepanion addon;
  private final CubepanionManager manager;

  private int buffer = 0;

  private String previousText;

  public ScoreboardListener(Cubepanion addon) {
    this.addon = addon;
    this.manager = this.addon.getManager();

    this.previousText = "";
  }

  @Subscribe
  public void serverIdTracker(ScoreboardTeamUpdateEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }

    String t = ((TextComponent) e.team().getPrefix()).getText();
    Matcher matcher = DATE_SERVER_ID_REGEX.matcher(t);
    if (matcher.matches()) {
      String serverId = matcher.group(1);
      this.updateServerId(serverId);
    }
  }

  @Subscribe
  public void mapTracker(ScoreboardTeamUpdateEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }

    List<Component> children = e.team().getPrefix().getChildren();

    switch (manager.getDivision()) {
      case FFA -> {
        if (children.size() < 2) {
          return;
        }

        List<Component> ffaComponent = children.getFirst().getChildren();
        if (ffaComponent.size() == 2) {
          if (((TextComponent) ffaComponent.getFirst()).getText().contains("Map")) {
            this.manager.setMapName(((TextComponent) children.get(1)).getText());
          }
        }
      }
      case LOBBY -> this.manager.setMapName("Main Lobby");
      default -> {
        if (this.previousText.trim().equals("Map") || this.previousText.trim().equals("Dimension")) {
          var text = ((TextComponent) e.team().getPrefix()).getText();
          if (!text.isBlank()) {
            this.manager.setMapName(text);
            this.previousText = ""; // Ensure the if con doesn't get called
          }
        }

        if (!e.team().getPrefix().getChildren().isEmpty()) {
          var text = ((TextComponent) children.getFirst()).getText();
          if (!text.isBlank()) {
            this.previousText = text;
          }
        }
      }
    }
  }

  private void updateServerId(String serverId) {
    this.manager.setServerID(serverId);
  }

  private CubeGame extractDivisionFromEvent(ScoreboardObjectiveUpdateEvent e) {
    Component title = e.objective().getTitle();
    String titleText = ((TextComponent) title).getText();

    if (titleText != null && !titleText.isEmpty() && titleText.matches("[a-zA-Z ]*")) {
      return CubeGame.stringToGame(titleText.trim());
    }

    for (Component child : title.getChildren()) {
      String text = ((TextComponent) child).getText();
      if (text == null) {
        continue;
      }

      text = text.replaceAll("[^a-zA-Z \\.]", "").trim();
      if (text.matches("[a-zA-Z ]+")) {
        return CubeGame.stringToGame(text.trim());
      }
    }

    return null;
  }

  @Subscribe
  public void divisionTracker(ScoreboardObjectiveUpdateEvent e) {
    if (!this.manager.onCubeCraft()) {
      return;
    }
    if (!e.objective().getName().equals("sidebar")) {
      return;
    }

    var division = this.extractDivisionFromEvent(e);
    if (division == null) return;

    if (CubeGame.isParkour(division)) {
      // I don't remember what the issue was with parkour, ignore it all.
      return;
    }


    this.buffer++;
    if (this.buffer % 3 != 0) return;

    this.buffer = 0;

    this.manager.setDivision(division);
  }

}
