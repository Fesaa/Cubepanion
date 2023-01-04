package org.ccu.core;

import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonListener;
import org.ccu.core.commands.AppealSiteCommand;
import org.ccu.core.commands.StatCommands;
import org.ccu.core.config.CCUconfig;
import org.ccu.core.config.internal.ConfigManager;
import org.ccu.core.listener.ChatReceiveEventListener;
import org.ccu.core.listener.GameShutdownEventListener;
import org.ccu.core.listener.GameTickEventListener;
import org.ccu.core.listener.NetworkListener;

@Singleton
@AddonListener
public class CCU extends LabyAddon<CCUconfig> {

  public ConfigManager CCUconfig;
  public DiscordRPCManager rpcManager;
  public WidgetManager widgetManager;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.CCUconfig = new ConfigManager(this);
    this.CCUconfig.initConfig();
    this.CCUconfig.loadConfig();

    this.rpcManager = new DiscordRPCManager(this);
    this.widgetManager = new WidgetManager(this);

    this.registerCommand(StatCommands.class);
    this.registerCommand(AppealSiteCommand.class);

    this.registerListener(NetworkListener.class);
    this.registerListener(ChatReceiveEventListener.class);
    this.registerListener(GameTickEventListener.class);
    this.registerListener(GameShutdownEventListener.class);

    this.widgetManager.register();


  }

  public Component prefix() {
    return Component.text("[", NamedTextColor.GRAY)
        .append(Component.text("CCU", NamedTextColor.GOLD))
        .append(Component.text("] ", NamedTextColor.GRAY));
  }

  @Override
  protected Class<CCUconfig> configurationClass() {
    return CCUconfig.class;
  }
}
