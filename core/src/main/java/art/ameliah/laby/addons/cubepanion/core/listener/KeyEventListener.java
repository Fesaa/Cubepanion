package art.ameliah.laby.addons.cubepanion.core.listener;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.GameMapInfoSubConfig;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.QOLMapSelectorLink;
import art.ameliah.laby.addons.cubepanion.core.weave.GameMapAPI;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.options.MinecraftInputMapping;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;
import net.labymod.api.event.client.input.MouseButtonEvent;
import net.labymod.api.event.client.input.MouseButtonEvent.Action;

public class KeyEventListener {

  private final Cubepanion addon;
  private final MinecraftInputMapping dropKey;

  private final QOLMapSelectorLink qolMapSelectorLink;


  public KeyEventListener(Cubepanion addon, QOLMapSelectorLink qolMapSelectorLink) {
    this.addon = addon;
    this.dropKey = this.addon.labyAPI().minecraft().options().getInputMapping("key.drop");
    this.qolMapSelectorLink = qolMapSelectorLink;
  }

  @Subscribe
  public void onMouseButtonEvent(MouseButtonEvent e) {
    if (this.addon.getManager().onCubeCraft()
        && this.addon.getManager().getDivision().equals(CubeGame.LOBBY)
        && e.action().equals(Action.CLICK)
        && this.addon.labyAPI().minecraft().minecraftWindow().isScreenOpened()) {
      if (this.qolMapSelectorLink != null
          && this.addon.configuration().getQolConfig().getMapSelector().get()) {
        this.qolMapSelectorLink.onScreenOpen();
      }
    }
  }

  @Subscribe
  public void onKeyEvent(KeyEvent keyEvent) {
    if (!this.addon.labyAPI().minecraft().isMouseLocked()) {
      return;
    }
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }

    // Game Map Info KeyBind
    GameMapInfoSubConfig subConfig = this.addon.configuration().getGameMapInfoSubConfig();
    if (keyEvent.key().equals(subConfig.getKey().get()) && subConfig.isEnabled().get()
        && GameMapAPI.getInstance().hasMaps(this.addon.getManager().getDivision())) {
      if (keyEvent.state() == State.PRESS) {
        this.addon.getManager().getGameMapInfoManager()
            .doGameMapLayout(this.addon.getManager().getMapName());
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
            Component.translatable("cubepanion.messages.preventedDrop").color(Colours.Error), true);
      }
    }
  }

  public boolean isTool(ItemStack itemStack) {

    if (itemStack.isSword()) {
      return true;
    }

    String itemString = itemStack.getAsItem().getIdentifier().getPath();

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
