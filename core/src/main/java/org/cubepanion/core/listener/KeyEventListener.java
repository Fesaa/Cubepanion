package org.cubepanion.core.listener;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.options.MinecraftInputMapping;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.EggWarsMapInfoSubConfig;
import org.cubepanion.core.utils.Colours;
import org.cubepanion.core.utils.CubeGame;

public class KeyEventListener {

  private final Cubepanion addon;
  private final MinecraftInputMapping dropKey;


  public KeyEventListener(Cubepanion addon) {
    this.addon = addon;
    this.dropKey = this.addon.labyAPI().minecraft().options().getInputMapping("key.drop");
  }

  @Subscribe
  public void onKeyEvent(KeyEvent keyEvent) {
    if (!this.addon.labyAPI().minecraft().isMouseLocked()) {
      return;
    }

    // EggWars Map Info KeyBind
    EggWarsMapInfoSubConfig subConfig = this.addon.configuration().getEggWarsMapInfoSubConfig();
    if (keyEvent.key().equals(subConfig.getKey().get()) && subConfig.isEnabled().get()) {
      if (keyEvent.state() == State.PRESS) {
        this.addon.getManager().getEggWarsMapInfoManager().doEggWarsMapLayout(this.addon.getManager().getMapName(), true);
      }
    }

    // Copy Server ID
    if (keyEvent.key().equals(this.addon.configuration().getAutomationConfig().getCopyServerID().get())) {
      if (keyEvent.state() == State.PRESS) {
        this.addon.labyAPI().minecraft().setClipboard(this.addon.getManager().getServerID());
      }
    }

    // Copy Bungeecord
    if (keyEvent.key().equals(this.addon.configuration().getAutomationConfig().getCopyBungeecord().get())) {
      if (keyEvent.state() == State.PRESS) {
        this.addon.labyAPI().minecraft().setClipboard(this.addon.getManager().getBungeecord());
      }
    }

    // No drop SkyBlock
    if (this.addon.getManager().getDivision().equals(CubeGame.SKYBLOCK)
    && this.addon.configuration().getQolConfig().getNoDropSkyBlock().get()) {
      if (this.dropKey.getKeyCode() != keyEvent.key().getId()) {
        return;
      }

      Player player = this.addon.labyAPI().minecraft().getClientPlayer();
      if (player == null) {
        return;
      }

      ItemStack mainHand = player.getMainHandItemStack();
      if (this.isTool(mainHand)) {
        keyEvent.setCancelled(true);
        this.addon.labyAPI().minecraft().chatExecutor().displayClientMessage(
            Component.text("Prevented you from dropping a tool!", Colours.Error),
            true
        );
      }
    }
  }

  public boolean isTool(ItemStack itemStack) {

    if (itemStack.isSword()) {
      return true;
    }

    String itemString = itemStack.getAsItem().toString();


    if (itemString.contains("_pickaxe")) {
      return true;
    }
    if (itemString.contains("_axe")) {
      return true;
    }
    if (itemString.contains("_shovel")) {
      return true;
    }
    if (itemString.contains("_hoe")) {
      return true;
    }
    if (itemString.contains("bow")) {
      return true;
    }

    if (itemString.contains("_helmet")) {
      return true;
    }
    if (itemString.contains("_chestplate")) {
      return true;
    }
    if (itemString.contains("_leggings")) {
      return true;
    }
    if (itemString.contains("_boots")) {
      return true;
    }

    return false;
  }

}
