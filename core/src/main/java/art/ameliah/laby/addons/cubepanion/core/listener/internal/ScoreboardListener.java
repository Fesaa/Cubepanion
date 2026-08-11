package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.PrintDebugRequestEvent;
import art.ameliah.laby.addons.cubepanion.core.events.PrintDebugRequestEvent.Receiver;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.external.Game;
import art.ameliah.laby.addons.cubepanion.core.external.GameFlag;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.scoreboard.ScoreboardObjectiveUpdateEvent;
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

    if (manager.getGame().hasFlagEnabled(GameFlag.LOBBY)) {
      this.manager.setMapName("Main Lobby");
      return;
    }

    if (manager.getGame().hasFlagEnabled(GameFlag.ALTERNATE_MAP_TRACKER)) {
      if (children.size() < 2) {
        return;
      }

      List<Component> ffaComponent = children.getFirst().getChildren();
      if (ffaComponent.size() == 2) {
        if (((TextComponent) ffaComponent.getFirst()).getText().contains("Map")) {
          this.manager.setMapName(((TextComponent) children.get(1)).getText());
        }
      }

      return;
    }

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

  private void updateServerId(String serverId) {
    this.manager.setServerID(serverId);
  }

  private Game extractDivisionFromEvent(ScoreboardObjectiveUpdateEvent e) {
    Component title = e.objective().getTitle();
    String titleText = ((TextComponent) title).getText();

    if (titleText != null && !titleText.isEmpty() && titleText.matches("[a-zA-Z ]*")) {
      return CubepanionAPI.I().tryGame(titleText.trim());
    }

    for (Component child : title.getChildren()) {
      String text = ((TextComponent) child).getText();
      if (text == null) {
        continue;
      }

      text = text.replaceAll("[^a-zA-Z \\.]", "").trim();
      if (text.matches("[a-zA-Z ]+")) {
        return CubepanionAPI.I().tryGame(text.trim());
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

    if (division.hasFlagEnabled(GameFlag.IGNORE_SCOREBOARD_UPDATES)) {
      return;
    }

    this.buffer++;
    if (this.buffer % 3 != 0) return;

    this.buffer = 0;

    this.manager.setGame(division);
  }

  @Subscribe
  public void onPrintDebugRequest(PrintDebugRequestEvent e) {
    if (e.receiver() == Receiver.ScoreboardListener) {
      Laby.labyAPI().minecraft().chatExecutor().chat(this.debug(), false);
    }
  }

  private String debug() {
    return String.format(
        "ScoreboardListener{" +
            "onCubeCraft=%s, " +
            "division=%s, " +
            "map=%s, " +
            "serverId=%s, " +
            "buffer=%d, " +
            "previousText='%s'" +
            "}",
        manager.onCubeCraft(),
        manager.getGame(),
        manager.getMapName(),
        manager.getServerID(),
        buffer,
        previousText
    );
  }

}
