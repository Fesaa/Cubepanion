package art.ameliah.laby.addons.cubepanion.core.gui.hud.nametags;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.QOLConfig;
import art.ameliah.laby.addons.cubepanion.core.config.QOLConfig.DisplayLocation;
import art.ameliah.laby.addons.cubepanion.core.events.GameJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.event.HoverEvent.Action;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.component.format.Style;
import net.labymod.api.client.component.format.TextColor;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.entity.player.tag.tags.NameTagBackground;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.network.server.SubServerSwitchEvent;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;

public class LevelTag extends NameTag {

  private static final Logging LOGGER = Logging.create(LevelTag.class);

  private final Cubepanion addon;
  private final QOLConfig config;
  private final Map<String, Integer> levels = new HashMap<>();
  private final Pattern whoList = Pattern.compile("There are \\d{1,3} players online: .*");
  private final Pattern joinMessage = Pattern.compile(
      "\\[\\+\\] (?:.{0,5} |)([a-zA-Z0-9_]{2,16})(?: .{0,5}|) joined your game \\(\\d{1,2}\\/\\d{1,2}\\)\\.");

  private boolean readingWhoMessage;
  private boolean readingJoinMessage;

  public LevelTag(Cubepanion addon) {
    this.addon = addon;
    this.config = addon.configuration().getQolConfig();

    this.addon.registerCubepanionListener(this);
  }

  @Subscribe
  public void onServerSwitch(SubServerSwitchEvent e) {
    this.levels.clear();
  }

  @Subscribe
  public void onServerSwitch(GameJoinEvent e) {
    if (e.getDestination().equals(CubeGame.LOBBY)) {
      this.readingWhoMessage = false;
      this.readingJoinMessage = false;
      return;
    }

    this.readingWhoMessage = true;
    this.readingJoinMessage = true;
    Laby.labyAPI().minecraft().chatExecutor().chat("/who", false);
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    if (this.readingWhoMessage) {
      Matcher m = this.whoList.matcher(e.chatMessage().getPlainText());
      if (m.matches()) {
        this.readWho((TextComponent) e.message());
        this.readingWhoMessage = false;
        e.setCancelled(true);
      }
    }

    if (readingJoinMessage) {
      Matcher m = this.joinMessage.matcher(e.chatMessage().getPlainText());
      if (m.matches()) {
        this.readJoinMessage((TextComponent) e.message());
      }
    }
  }

  private void readWho(TextComponent msg) {
    List<Component> children = msg.getChildren();
    int groupSize = 0;
    for (int i = 0; i < children.size(); i++) {
      TextComponent t = (TextComponent) children.get(i);
      groupSize++;

      if (t.getText().contains("players online:")) {
        groupSize = 0;
        continue;
      }

      if (t.getText().equals(", ") || i == children.size() - 1) {
        int offSet = i == children.size() - 1 ? 1 : 0;
        if (i != children.size() - 1) {
          groupSize--;
        }

        if (groupSize == 2) { // Rank and name
          this.readAndPutLevel((TextComponent) children.get(i - 1 + offSet));
        } else if (groupSize == 4) { // Rank, name, and emotes
          this.readAndPutLevel((TextComponent) children.get(i - 2 + offSet));
        }
        groupSize = 0;
      }
    }
  }

  private void readJoinMessage(TextComponent msg) {
    int size = msg.getChildren().size();
    TextComponent name = null;
    if (size == 4) { // Name
      name = (TextComponent) msg.getChildren().get(2);
    } else if (size == 6) { // Name and emotes
      name = (TextComponent) msg.getChildren().get(3);
    }
    if (name == null) {
      return;
    }
    this.readAndPutLevel(name);
  }

  private void readAndPutLevel(TextComponent name) {
    // Players may rejoin the name, lets not try and parse an int again.
    String playerName = this.getPlayerName(name);
    if (this.levels.containsKey(playerName)) {
      return;
    }

    Style style = name.style();
    if (style == null) {
      return;
    }
    HoverEvent<?> e = style.getHoverEvent();
    if (e == null || e.action() != Action.SHOW_TEXT) {
      return;
    }
    TextComponent hoverText = (TextComponent) e.value();
    int hoverSize = hoverText.getChildren().size();
    TextComponent level = (TextComponent) hoverText.getChildren().get(hoverSize - 1);

    int levelInt;
    try {
      levelInt = Integer.parseInt(level.getText());
    } catch (NumberFormatException ex) {
      LOGGER.debug("Could not parse level from join message", ex);
      return;
    }

    this.levels.put(playerName, levelInt);
  }

  @NotNull
  private String getPlayerName(TextComponent name) {
    if (name.getChildren().isEmpty()) {
      return name.getText().trim();
    }

    // Name is split into multiple components, lets combine them.
    // This is the case for multi-coloured names.
    StringBuilder playerName = new StringBuilder();
    for (int i = 0; i < name.getChildren().size(); i++) {
      TextComponent child = (TextComponent) name.getChildren().get(i);
      playerName.append(child.getText().trim());
    }
    return playerName.toString();
  }

  private TextColor getLevelColour(int level) {
    if (level < 50) {
      return NamedTextColor.GRAY;
    } else if (level < 100) {
      return NamedTextColor.GREEN;
    } else if (level < 150) {
      return NamedTextColor.BLUE;
    } else if (level < 200) {
      return NamedTextColor.DARK_PURPLE;
    } else if (level < 250) {
      return NamedTextColor.GOLD;
    } else if (level < 300) {
      return NamedTextColor.RED;
    } else {
      return NamedTextColor.DARK_RED;
    }
  }

  @Override
  public RenderableComponent getRenderableComponent() {
    if (!config.getLevelTag().get()) {
      return null;
    }
    if (!this.shouldRender()) {
      return null;
    }
    if (!(this.entity instanceof Player player)) {
      return null;
    }

    Integer level = this.levels.get(player.getName());
    if (level == null) {
      return null;
    }

    return RenderableComponent.of(Component.text(level, this.getLevelColour(level)));
  }

  private boolean shouldRender() {
    DisplayLocation displayLocation = this.config.getLevelTagDisplayLocation().get();
    return switch (displayLocation) {
      case BOTH -> true;
      case PRE_LOBBY -> this.addon.getManager().isInPreLobby();
      case GAME -> !this.addon.getManager().isInPreLobby();
    };
  }

  @Override
  public NameTagBackground getCustomBackground() {
    return NameTagBackground.custom(false, 0);
  }
}
