package org.ccu.core.listener;

import com.google.inject.Inject;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.config.ConfigurationSaveEvent;
import org.ccu.core.CCU;

public class ConfigurationSaveEventListener {

  private final CCU addon;

  @Inject
  public ConfigurationSaveEventListener(CCU addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onConfigurationSaveEvent(ConfigurationSaveEvent e) {
    this.addon.commandManager.updateCommandRegistration();
  }

}
