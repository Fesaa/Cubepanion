package org.ccu.core;

import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.models.addon.annotation.AddonMain;
import org.ccu.core.commands.AppealSiteCommand;
import org.ccu.core.commands.EggWarsMapInfoCommand;
import org.ccu.core.commands.PartyCommands;
import org.ccu.core.commands.StatCommands;
import org.ccu.core.config.CCUManager;
import org.ccu.core.config.CCUconfig;
import org.ccu.core.listener.Chat.Automations;
import org.ccu.core.listener.Chat.PartyTracker;
import org.ccu.core.listener.Chat.StatsTracker;
import org.ccu.core.listener.GameShutdownEventListener;
import org.ccu.core.listener.GameTickEventListener;
import org.ccu.core.listener.KeyEventListener;
import org.ccu.core.listener.PlayerNameTagRenderListener;
import org.ccu.core.listener.network.PlayerInfo;
import org.ccu.core.listener.network.ServerNavigation;

@AddonMain
public class CCU extends LabyAddon<CCUconfig> {
  public DiscordRPCManager rpcManager;
  public WidgetManager widgetManager;


  private CCUManager manager;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.manager = new CCUManager(this);

    this.rpcManager = new DiscordRPCManager(this);
    this.widgetManager = new WidgetManager(this);

    this.registerCommand(new PartyCommands(this));
    this.registerCommand(new AppealSiteCommand(this));
    this.registerCommand(new EggWarsMapInfoCommand(this));
    this.registerCommand(new StatCommands(this));

    this.registerListener(new PlayerInfo(this));
    this.registerListener(new ServerNavigation(this));
    this.registerListener(new GameTickEventListener(this));
    this.registerListener(new GameShutdownEventListener(this));
    this.registerListener(new PlayerNameTagRenderListener(this));
    this.registerListener(new KeyEventListener(this));
    this.registerListener(new Automations(this));
    this.registerListener(new PartyTracker(this));
    this.registerListener(new StatsTracker(this));

    this.widgetManager.register();

    this.logger().info("CubeCraft-Utilities has successfully registered all her components.");
  }

  public CCUManager getManager() {
    return this.manager;
  }

  public Component prefix() {
    return Component.text("[", Colours.Title)
        .append(Component.text("CCU", Colours.Primary))
        .append(Component.text("] ", Colours.Title));
  }

  @Override
  protected Class<CCUconfig> configurationClass() {
    return CCUconfig.class;
  }
}
