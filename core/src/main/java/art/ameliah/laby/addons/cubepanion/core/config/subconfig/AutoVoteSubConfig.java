package art.ameliah.laby.addons.cubepanion.core.config.subconfig;

import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.notification.Notification;
import net.labymod.api.notification.Notification.NotificationButton;
import net.labymod.api.notification.Notification.Type;

public class AutoVoteSubConfig extends Config {

  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(false);
  @DropdownSetting
  @SpriteSlot(x = 1, y = 1)
  private final ConfigProperty<ThreeOptionsMode> eggWarsItems = new ConfigProperty<>(
      ThreeOptionsMode.RIGHT);
  @DropdownSetting
  @SpriteSlot(x = 7)
  private final ConfigProperty<ThreeOptionsMode> eggWarsHealth = new ConfigProperty<>(
      ThreeOptionsMode.MIDDLE);
  @DropdownSetting
  private final ConfigProperty<TwoOptionsMode> eggWarsPerk = new ConfigProperty<>(
      TwoOptionsMode.RIGHT);
  @DropdownSetting
  @SpriteSlot(y = 1)
  private final ConfigProperty<ThreeOptionsMode> skyWarsChests = new ConfigProperty<>(
      ThreeOptionsMode.RIGHT);
  @DropdownSetting
  @SpriteSlot(x = 2, y = 1)
  private final ConfigProperty<ThreeOptionsMode> skyWarsProjectiles = new ConfigProperty<>(
      ThreeOptionsMode.LEFT);
  @DropdownSetting
  @SpriteSlot(x = 3, y = 1)
  private final ConfigProperty<ThreeOptionsMode> skyWarsTime = new ConfigProperty<>(
      ThreeOptionsMode.LEFT);
  @DropdownSetting
  @SpriteSlot(x = 4, y = 1)
  private final ConfigProperty<FourOptionsMode> luckyIslandsBlocks = new ConfigProperty<>(
      FourOptionsMode.RIGHT);
  @DropdownSetting
  @SpriteSlot(x = 3, y = 1)
  private final ConfigProperty<ThreeOptionsMode> luckyIslandsTime = new ConfigProperty<>(
      ThreeOptionsMode.LEFT);
  @SwitchSetting
  private final ConfigProperty<Boolean> experiments = new ConfigProperty<>(false);

  public AutoVoteSubConfig() {
    enabled.addChangeListener((prop, oldV, newV) -> {
      if (oldV || !newV) {
        return;
      }

      if (experiments.get()) {
        return;
      }

      Notification notification = Notification
          .builder()
          .type(Type.SYSTEM)
          .title(Component.translatable("cubepanion.notifications.experimentsIsOff.title",
              Colours.Primary))
          .text(Component.translatable("cubepanion.notifications.experimentsIsOff.text",
              Colours.Secondary))
          .addButton(NotificationButton.of(
              Component.translatable("cubepanion.notifications.experimentsIsOff.enable"),
              () -> experiments.set(true)))
          .duration(5000)
          .build();
      Laby.labyAPI().notificationController().push(notification);
    });
  }

  public boolean isEnabled() {
    return this.enabled.get();
  }

  public ConfigProperty<FourOptionsMode> getLuckyIslandsBlocks() {
    return luckyIslandsBlocks;
  }

  public ConfigProperty<ThreeOptionsMode> getEggWarsHealth() {
    return eggWarsHealth;
  }

  public ConfigProperty<ThreeOptionsMode> getEggWarsItems() {
    return eggWarsItems;
  }

  public ConfigProperty<TwoOptionsMode> getEggWarsPerk() {
    return eggWarsPerk;
  }

  public ConfigProperty<ThreeOptionsMode> getLuckyIslandsTime() {
    return luckyIslandsTime;
  }

  public ConfigProperty<ThreeOptionsMode> getSkyWarsChests() {
    return skyWarsChests;
  }

  public ConfigProperty<ThreeOptionsMode> getSkyWarsProjectiles() {
    return skyWarsProjectiles;
  }

  public ConfigProperty<ThreeOptionsMode> getSkyWarsTime() {
    return skyWarsTime;
  }

  public ConfigProperty<Boolean> getExperiments() {
    return experiments;
  }

  public enum TwoOptionsMode {
    LEFT(12),
    RIGHT(14),
    NONE(-1);

    public final int slot;

    TwoOptionsMode(int slot) {
      this.slot = slot;
    }
  }

  public enum FourOptionsMode {
    LEFT(10),
    MIDDLE_LEFT(12),
    MIDDLE_RIGHT(14),
    RIGHT(16),
    NONE(-1);
    public final int slot;

    FourOptionsMode(int slot) {
      this.slot = slot;
    }
  }

  public enum ThreeOptionsMode {
    LEFT(11),
    MIDDLE(13),
    RIGHT(15),
    NONE(-1);
    public final int slot;

    ThreeOptionsMode(int slot) {
      this.slot = slot;
    }

  }

}