package org.ccu.core;

import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonListener;
import org.ccu.core.commands.AppealSiteCommand;
import org.ccu.core.commands.EggWarsMapInfoCommand;
import org.ccu.core.commands.StatCommands;
import org.ccu.core.config.CCUManager;
import org.ccu.core.config.CCUconfig;
import org.ccu.core.gui.imp.ClientPlayerSpawnProtection;
import org.ccu.core.listener.ChatReceiveEventListener;
import org.ccu.core.listener.GameShutdownEventListener;
import org.ccu.core.listener.GameTickEventListener;
import org.ccu.core.listener.NetworkListener;
import org.ccu.core.listener.PlayerNameTagRenderListener;

@Singleton
@AddonListener
public class CCU extends LabyAddon<CCUconfig> {
  public DiscordRPCManager rpcManager;
  public WidgetManager widgetManager;
  public ClientPlayerSpawnProtection clientPlayerSpawnProtection;

  private CCUManager manager;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.manager = new CCUManager(this);

    this.clientPlayerSpawnProtection = new ClientPlayerSpawnProtection(this);

    this.rpcManager = new DiscordRPCManager(this);
    this.widgetManager = new WidgetManager(this);

    this.registerCommand(StatCommands.class);
    this.registerCommand(AppealSiteCommand.class);
    this.registerCommand(EggWarsMapInfoCommand.class);

    this.registerListener(NetworkListener.class);
    this.registerListener(ChatReceiveEventListener.class);
    this.registerListener(GameTickEventListener.class);
    this.registerListener(GameShutdownEventListener.class);
    this.registerListener(PlayerNameTagRenderListener.class);

    this.widgetManager.register();


  }

  public CCUManager getManager() {
    return this.manager;
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
