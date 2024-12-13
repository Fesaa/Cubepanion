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
      this.manager.setServerID(serverId);
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

    boolean updatedMap = false;
    switch (manager.getDivision()) {
      case FFA -> {
        List<Component> ffaComponent = children.get(0).getChildren();
        if (ffaComponent.size() == 2) {
          if (((TextComponent) ffaComponent.get(0)).getText().contains("Map: ")) {
            this.manager.setMapName(((TextComponent) ffaComponent.get(1)).getText());
            updatedMap = true;
          }
        }
      }
      case LOBBY -> {
        if (updatedDivision) {
          this.manager.setMapName("Main Lobby");
          updatedMap = true;
        }
      }
      default -> {
        String text = ((TextComponent) children.get(0)).getText();
        if (this.previousText.equals("Map:") || this.previousText.equals("Dimension:")) {
          this.manager.setMapName(text);
          updatedMap = true;
        }
        this.previousText = text;
      }
    }
    if (updatedMap) {
      Laby.fireEvent(new RequestEvent(RequestType.UPDATE_RPC));
    }
  }

  @Subscribe
  public void onGameStart(GameStartEvent e) {
    this.updatedDivision = true;
    this.cooldown = 0;
  }

  private void updateDivision(CubeGame division) {
    this.cooldown++;

    if (this.cooldown != 1 || this.updatedDivision) {

      // EggWars has a pre-lobby, so it fires it 2*3 times; Hard code this in
      int threshHold = this.manager.getDivision().equals(CubeGame.TEAM_EGGWARS) ? 6 : 3;

      if (this.cooldown == threshHold) {
        this.cooldown = 0;
        this.updatedDivision = false;
      }

      return;
    }

    this.manager.setDivision(division);
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
