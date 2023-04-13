package org.cubecraftutilities.core.config.subconfig;

import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownEntryTranslationPrefix;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget.DropdownSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class AutoVoteSubConfig extends Config {

  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @DropdownSetting
  @SpriteSlot(x = 1, y = 1)
  @DropdownEntryTranslationPrefix("cubecraftutilities.settings.autoVoteSubConfig.eggWarsItems.entries")
  private final ConfigProperty<ThreeOptionsMode> eggWarsItems = new ConfigProperty<>(ThreeOptionsMode.RIGHT);

  @DropdownSetting
  @SpriteSlot(x = 7)
  @DropdownEntryTranslationPrefix("cubecraftutilities.settings.autoVoteSubConfig.eggWarsHealth.entries")
  private final ConfigProperty<ThreeOptionsMode> eggWarsHealth = new ConfigProperty<>(ThreeOptionsMode.LEFT);

  @DropdownSetting
  @SpriteSlot(y = 1)
  @DropdownEntryTranslationPrefix("cubecraftutilities.settings.autoVoteSubConfig.skyWarsChests.entries")
  private final ConfigProperty<ThreeOptionsMode> skyWarsChests = new ConfigProperty<>(ThreeOptionsMode.RIGHT);

  @DropdownSetting
  @SpriteSlot(x = 2, y = 1)
  @DropdownEntryTranslationPrefix("cubecraftutilities.settings.autoVoteSubConfig.skyWarsProjectiles.entries")
  private final ConfigProperty<ThreeOptionsMode> skyWarsProjectiles = new ConfigProperty<>(ThreeOptionsMode.RIGHT);

  @DropdownSetting
  @SpriteSlot(x = 3, y = 1)
  @DropdownEntryTranslationPrefix("cubecraftutilities.settings.autoVoteSubConfig.skyWarsTime.entries")
  private final ConfigProperty<ThreeOptionsMode> skyWarsTime = new ConfigProperty<>(ThreeOptionsMode.RIGHT);

  @DropdownSetting
  @SpriteSlot(x = 4, y = 1)
  @DropdownEntryTranslationPrefix("cubecraftutilities.settings.autoVoteSubConfig.luckyIslandsBlocks.entries")
  private final ConfigProperty<FourOptionsMode> luckyIslandsBlocks = new ConfigProperty<>(FourOptionsMode.LEFT);

  @DropdownSetting
  @SpriteSlot(x = 3, y = 1)
  @DropdownEntryTranslationPrefix("cubecraftutilities.settings.autoVoteSubConfig.luckyIslandsTime.entries")
  private final ConfigProperty<ThreeOptionsMode> luckyIslandsTime = new ConfigProperty<>(ThreeOptionsMode.MIDDLE);


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

  public enum FourOptionsMode {
    LEFT(10),
    MIDDLE_LEFT(12),
    MIDDLE_RIGHT(14),
    RIGHT(16);
    public final int slot;
    FourOptionsMode(int slot) {
      this.slot = slot;
    }
  }

  public enum ThreeOptionsMode {
    LEFT(11),
    MIDDLE(13),
    RIGHT(15);
    public final int slot;
    ThreeOptionsMode(int slot) {
      this.slot = slot;
    }

  }

}