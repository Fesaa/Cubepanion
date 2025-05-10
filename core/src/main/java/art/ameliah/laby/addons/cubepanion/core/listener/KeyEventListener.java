package art.ameliah.laby.addons.cubepanion.core.listener;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.GameMapInfoSubConfig;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.utils.Colours;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.gamemaps.AbstractGameMap;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.options.MinecraftInputMapping;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.event.client.input.KeyEvent.State;

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
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }

    // Game Map Info KeyBind
    GameMapInfoSubConfig subConfig = this.addon.configuration().getGameMapInfoSubConfig();
    if (keyEvent.key().equals(subConfig.getKey().get()) && subConfig.isEnabled().get()
        && CubepanionAPI.I().hasMaps(this.addon.getManager().getDivision())) {
      if (keyEvent.state() == State.PRESS) {
        AbstractGameMap map = CubepanionAPI.I().currentMap();
        this.addon.getManager().getGameMapInfoManager().displayGameMapLayout(map);
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
