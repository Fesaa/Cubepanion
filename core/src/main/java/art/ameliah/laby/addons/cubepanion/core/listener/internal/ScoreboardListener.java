package art.ameliah.laby.addons.cubepanion.core.listener.internal;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.GameStartEvent;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent.RequestType;
import art.ameliah.laby.addons.cubepanion.core.managers.CubepanionManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardObjectiveUpdateEvent;
import net.labymod.api.event.client.scoreboard.ScoreboardTeamEntryAddEvent;

public class ScoreboardListener {

  private static final Pattern DATE_SERVER_ID_REGEX = Pattern.compile(
      "[0-9]{2}/[0-9]{2}/[0-9]{2} \\((.{5})\\)");

  private final Cubepanion addon;
  private final CubepanionManager manager;

  private int cooldown;
  private CubeGame bufferedDivision;

  private String previousText;
  private boolean updatedDivision;

  public ScoreboardListener(Cubepanion addon) {
    this.addon = addon;
    this.manager = this.addon.getManager();

    this.previousText = "";
  }

  @Subscribe
  public void onServerSwitch(SubServerSwitchEvent e) {
    this.updatedDivision = false;
  }

  @Subscribe
  public void serverIdTracker(ScoreboardTeamEntryAddEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }

    List<Component> children = e.team().getPrefix().getChildren();
    if (children.isEmpty()) {
      return;
    }

    String t = ((TextComponent) children.getFirst()).getText();
    Matcher matcher = DATE_SERVER_ID_REGEX.matcher(t);
    if (matcher.matches()) {
      String serverId = matcher.group(1);
      this.updateServerId(serverId);
    }
  }

  @Subscribe
  public void mapTracker(ScoreboardTeamEntryAddEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }

    List<Component> children = e.team().getPrefix().getChildren();
    if (children.isEmpty()) {
      return;
    }

    if (this.bufferedDivision == null) {
      //LOGGER.debug(getClass(), "No division found for map update, skipping");
      return;
    }

    switch (this.bufferedDivision) {
      case FFA -> {
        List<Component> ffaComponent = children.getFirst().getChildren();
        if (ffaComponent.size() == 2) {
          if (((TextComponent) ffaComponent.get(0)).getText().contains("Map: ")) {
            this.manager.setMapName(((TextComponent) ffaComponent.get(1)).getText());
          }
        }
      }
      case LOBBY -> {
        this.manager.setMapName("Main Lobby");
      }
      default -> {
        String text = ((TextComponent) children.getFirst()).getText();
        if (this.previousText.equals("Map:") || this.previousText.equals("Dimension:")) {
          this.manager.setMapName(text);
        }
        this.previousText = text;
      }
    }
  }

  @Subscribe
  public void onGameStart(GameStartEvent e) {
    this.updatedDivision = true;
    this.cooldown = 0;
  }

  private void updateServerId(String serverId) {
    this.manager.setServerID(serverId);
    if (this.bufferedDivision != null) {
      this.manager.setDivision(this.bufferedDivision);
      this.bufferedDivision = null;
      Laby.fireEvent(new RequestEvent(RequestType.UPDATE_RPC));
    }
  }

  private void updateDivision(CubeGame division) {
    this.cooldown++;

    // if it's a new division, always pass through
    if ((this.cooldown == 1 && !this.updatedDivision) || !this.manager.getDivision().equals(division)) {
      this.bufferedDivision = division;

      // In case of the new division, manually sync these back
      this.cooldown = 1;
      this.updatedDivision = false;
      return;
    }

    // EggWars has a pre-lobby, so it fires it 2*3 times; Hard code this in
    int threshHold = this.manager.getDivision().equals(CubeGame.TEAM_EGGWARS) ? 6 : 3;

    if (this.cooldown >= threshHold) {
      this.cooldown = 0;
      this.updatedDivision = false;
    }
  }

  @Subscribe
  public void divisionTracker(ScoreboardObjectiveUpdateEvent e) {
    if (!this.manager.onCubeCraft()) {
      return;
    }
    if (!e.objective().getName().equals("sidebar")) {
      return;
    }
    /*if (updatedDivision && !CubeGame.isParkour(this.manager.getDivision())) {
      return;
    }*/
    if (CubeGame.isParkour(this.manager.getDivision())) {
      // I don't remember what the issue was with parkour, ignore it all. 
      return;
    }

    Component title = e.objective().getTitle();
    String titleText = ((TextComponent) title).getText();
    if (titleText != null && !titleText.isEmpty() && titleText.matches("[a-zA-Z ]*")) {
      this.updateDivision(CubeGame.stringToGame(titleText.trim()));
    } else {
      for (Component child : title.getChildren()) {
        String text = ((TextComponent) child).getText();
        if (text == null) {
          continue;
        }
        text = text.replaceAll("[^a-zA-Z \\.]", "").trim();
        if (text.matches("[a-zA-Z ]+")) {
          this.updateDivision(CubeGame.stringToGame(text.trim()));
          break;
        }
      }
    }
  }

}
