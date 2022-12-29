package org.ccu.core;

import com.google.inject.Singleton;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonListener;
import org.ccu.core.config.CCUconfig;
import org.ccu.core.config.internal.ConfigManager;
import org.ccu.core.gui.hud.widgets.CustomWidgets;
import org.ccu.core.listener.ChatReceiveEventListener;
import org.ccu.core.listener.NetworkListener;

@Singleton
@AddonListener
public class CCU extends LabyAddon<CCUconfig> {

  public ConfigManager CCUconfig;
  public DiscordRPCManager rpcManager;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.CCUconfig = new ConfigManager(this);
    this.CCUconfig.initConfig();
    this.CCUconfig.loadConfig();

    this.rpcManager = new DiscordRPCManager(this);

    this.registerListener(NetworkListener.class);
    this.registerListener(ChatReceiveEventListener.class);

    CustomWidgets.RegisterWidgets(this);
  }

  @Override
  protected Class<CCUconfig> configurationClass() {
    return CCUconfig.class;
  }
}
