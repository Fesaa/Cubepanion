package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.ItemUseEvent;
import art.ameliah.laby.addons.cubepanion.core.events.ItemUseEvent.UseType;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.CooldownManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import net.labymod.api.event.Subscribe;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class Cooldowns {

  private final Cubepanion addon;
  private final @NotNull FunctionLink functionLink;
  private final Map<String, Long> cooldowns = new HashMap<>();

  public Cooldowns(Cubepanion addon, @NotNull FunctionLink functionLink) {
    this.addon = addon;
    this.functionLink = functionLink;

    this.cooldowns.put(CooldownManager.FIREBALL, CooldownManager.FIREBALL_COOLDOWN_TIME);
    this.cooldowns.put(CooldownManager.FEATHER, CooldownManager.FEATHER_COOLDOWN_TIME);
  }

  @Subscribe
  public void onItemUse(ItemUseEvent e) {
    if (!addon.getManager().isPlaying(CubeGame.TEAM_EGGWARS)) {
      return;
    }

    if (e.getUseType() != UseType.USE) {
      return;
    }

    String id = e.getItemStack().getAsItem().getIdentifier().getPath();
    Long cooldownTime = this.cooldowns.get(id);
    if (cooldownTime == null) {
      return;
    }

    if (addon.getManager().getCooldownManager().onCooldown(id, cooldownTime)) {
      LOGGER.debug(getClass(), "Cooldown " + id + " is already on cooldown: " + cooldownTime);
      return;
    }

    addon.getManager().getCooldownManager().setLastUse(id, System.currentTimeMillis());
    if (addon.configuration().getQolConfig().getCoolDown().get()) {
      functionLink.setCoolDown(e.getItemStack(), (int) ((cooldownTime/1000)*20) );
    }
  }

}
