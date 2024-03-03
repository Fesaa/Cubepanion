package art.ameliah.laby.addons.cubepanion.core.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.client.resources.ResourceLocation;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;

@SpriteTexture("setting_icons.png")
public class QOLConfig extends Config {

  @SwitchSetting
  private final ConfigProperty<Boolean> rankTag = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> levelTag = new ConfigProperty<>(false);

  @DropdownSetting
  private final ConfigProperty<DisplayLocation> levelTagDisplayLocation = new ConfigProperty<>(DisplayLocation.BOTH);

  @SwitchSetting
  private final ConfigProperty<Boolean> mapSelector = new ConfigProperty<>(false);
  @SwitchSetting
  private final ConfigProperty<Boolean> chestLocation = new ConfigProperty<>(true);
  @SliderSetting(min = 5F, max = 20F)
  private final ConfigProperty<Integer> range = new ConfigProperty<>(10);
  @SwitchSetting
  private final ConfigProperty<Boolean> noDropSkyBlock = new ConfigProperty<>(false);
  @SpriteSlot(x = 3, y = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> respawnTimer = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> fireBallCoolDown = new ConfigProperty<>(false);

  @SwitchSetting
  private final ConfigProperty<Boolean> reminderToVote = new ConfigProperty<>(false);
  @TextFieldSetting
  @SettingRequires(value = "reminderToVote")
  private final ConfigProperty<String> reminderToVoteSoundId = new ConfigProperty<>(
      "entity.lightning_bolt.impact");

  public ConfigProperty<Boolean> getRankTag() {
    return this.rankTag;
  }

  public ConfigProperty<Boolean> getLevelTag() {
    return this.levelTag;
  }

  public ConfigProperty<Boolean> getRespawnTimer() {
    return this.respawnTimer;
  }

  public ConfigProperty<Boolean> getReminderToVote() {
    return reminderToVote;
  }

  public ResourceLocation getVoteReminderResourceLocation() {
    return ResourceLocation.create("minecraft", this.reminderToVoteSoundId.get());
  }

  public ConfigProperty<Boolean> getNoDropSkyBlock() {
    return noDropSkyBlock;
  }

  public ConfigProperty<Boolean> getMapSelector() {
    return mapSelector;
  }

  public ConfigProperty<Boolean> getChestLocation() {
    return chestLocation;
  }

  public ConfigProperty<Integer> getRange() {
    return range;
  }

  public ConfigProperty<Boolean> getFireBallCoolDown() {
    return fireBallCoolDown;
  }

  public enum DisplayLocation {
    PRE_LOBBY, GAME, BOTH
  }
}
