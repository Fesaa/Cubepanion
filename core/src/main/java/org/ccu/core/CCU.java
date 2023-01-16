package org.ccu.core;

import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonListener;
import net.labymod.api.util.Pair;
import org.ccu.core.commands.AppealSiteCommand;
import org.ccu.core.commands.EggWarsMapInfoCommand;
import org.ccu.core.commands.PartyCommands;
import org.ccu.core.commands.StatCommands;
import org.ccu.core.config.CCUManager;
import org.ccu.core.config.CCUconfig;
import org.ccu.core.listener.Chat.Automations;
import org.ccu.core.listener.Chat.PartyTracker;
import org.ccu.core.listener.Chat.StatsTracker;
import org.ccu.core.listener.ConfigurationSaveEventListener;
import org.ccu.core.listener.GameShutdownEventListener;
import org.ccu.core.listener.GameTickEventListener;
import org.ccu.core.listener.KeyEventListener;
import org.ccu.core.listener.NetworkListener;
import org.ccu.core.listener.PlayerNameTagRenderListener;

@Singleton
@AddonListener
public class CCU extends LabyAddon<CCUconfig> {
  public DiscordRPCManager rpcManager;
  public WidgetManager widgetManager;
  public CommandManager commandManager;


  private CCUManager manager;

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.manager = new CCUManager(this);

    this.rpcManager = new DiscordRPCManager(this);
    this.widgetManager = new WidgetManager(this);
    this.commandManager = new CommandManager(this,
        Pair.of(this.configuration().getCommandSystemSubConfig()::getAppealSiteCommand, AppealSiteCommand.class),
        Pair.of(this.configuration().getCommandSystemSubConfig()::getStatsCommand, StatCommands.class),
        Pair.of(this.configuration().getCommandSystemSubConfig()::getEggWarsMapInfoCommand, EggWarsMapInfoCommand.class),
        Pair.of(this.configuration().getCommandSystemSubConfig()::getPartyCommands, PartyCommands.class));

    this.registerListener(NetworkListener.class);
    this.registerListener(GameTickEventListener.class);
    this.registerListener(GameShutdownEventListener.class);
    this.registerListener(PlayerNameTagRenderListener.class);
    this.registerListener(KeyEventListener.class);
    this.registerListener(Automations.class);
    this.registerListener(PartyTracker.class);
    this.registerListener(StatsTracker.class);
    this.registerListener(ConfigurationSaveEventListener.class);

    this.widgetManager.register();
    this.commandManager.updateCommandRegistration();

    this.logger().info("CubeCraft-Utilities has successfully registered all her components.");
  }

  public CCUManager getManager() {
    return this.manager;
  }

  public Component prefix() {
    return Component.text("[", NamedTextColor.GOLD)
        .append(Component.text("CCU", NamedTextColor.AQUA))
        .append(Component.text("] ", NamedTextColor.GOLD));
  }

  @Override
  protected Class<CCUconfig> configurationClass() {
    return CCUconfig.class;
  }
}
