package org.ccu.core;

import com.google.inject.Singleton;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonListener;
import org.ccu.core.config.CCUconfig;
import org.ccu.core.config.internal.ConfigManager;
import org.ccu.core.gui.hud.widgets.CounterItemHudWidget;
import org.ccu.core.gui.hud.widgets.DurabilityItemHudWidget;
import org.ccu.core.gui.hud.widgets.NextArmourBuyTextWidget;
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

    this.labyAPI().hudWidgetRegistry().register(new CounterItemHudWidget("emerald_counter","emerald", 3, 0));
    this.labyAPI().hudWidgetRegistry().register(new CounterItemHudWidget("diamond_counter","diamond", 1, 0));
    this.labyAPI().hudWidgetRegistry().register(new CounterItemHudWidget("iron_ingot_counter","iron_ingot", 0, 0));
    this.labyAPI().hudWidgetRegistry().register(new CounterItemHudWidget("gold_ingot_counter","gold_ingot", 2, 0));
    this.labyAPI().hudWidgetRegistry().register(new CounterItemHudWidget("terracotta_counter","(\\w{0,10}\\_{0,1}){0,2}terracotta", 4, 0));

    this.labyAPI().hudWidgetRegistry().register(new DurabilityItemHudWidget("helmet_durability_counter", "\\w{0,10}_helmet", 5, 0));
    this.labyAPI().hudWidgetRegistry().register(new DurabilityItemHudWidget("chestplate_durability_counter", "\\w{0,10}_chestplate", 6, 0));
    this.labyAPI().hudWidgetRegistry().register(new DurabilityItemHudWidget("leggings_durability_counter", "\\w{0,10}_leggings", 7, 0));
    this.labyAPI().hudWidgetRegistry().register(new DurabilityItemHudWidget("boots_durability_counter", "\\w{0,10}_boots", 0, 1));

    this.labyAPI().hudWidgetRegistry().register(new NextArmourBuyTextWidget("nextArmourDurability"));
  }

  @Override
  protected Class<CCUconfig> configurationClass() {
    return CCUconfig.class;
  }
}
