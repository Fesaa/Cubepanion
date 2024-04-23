package art.ameliah.laby.addons.cubepanion.core.config.subconfig;

import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent.RequestType;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.property.Property;
import net.labymod.api.util.function.ChangeListener;

public class DiscordRichPresenceSubConfig extends Config {

  @SwitchSetting
  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  @SwitchSetting
  @SpriteSlot(y = 2, x = 1)
  private final ConfigProperty<Boolean> map = new ConfigProperty<>(true);
  @SwitchSetting
  @SpriteSlot(y = 2)
  private final ConfigProperty<Boolean> players = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> gameImage = new ConfigProperty<>(false);

  public DiscordRichPresenceSubConfig() {

    ChangeListener<Property<Boolean>, Boolean> listener = (type, oldValue, newValue) -> {
      Laby.fireEvent(new RequestEvent(RequestType.UPDATE_RPC));
    };

    this.enabled.addChangeListener(listener);
    this.players.addChangeListener(listener);
    this.gameImage.addChangeListener(listener);
    this.map.addChangeListener(listener);
  }

  public boolean isEnabled() {
    return this.enabled.get();
  }

  public ConfigProperty<Boolean> map() {
    return this.map;
  }

  public ConfigProperty<Boolean> players() {
    return this.players;
  }

  public ConfigProperty<Boolean> getGameImage() {
    return gameImage;
  }
}
