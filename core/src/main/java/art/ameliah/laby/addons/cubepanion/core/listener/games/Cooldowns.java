package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.ItemUseEvent;
import art.ameliah.laby.addons.cubepanion.core.events.ItemUseEvent.UseType;
import art.ameliah.laby.addons.cubepanion.core.managers.submanagers.CooldownManager;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import java.util.HashMap;
import java.util.Map;
import net.labymod.api.event.Subscribe;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;

public class Cooldowns {

  private static final Logging log = Logging.create(Cubepanion.class.getSimpleName());

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
      log.debug("Cooldown event triggered for item {}, time {}", id, cooldownTime);
      return;
    }

    addon.getManager().getCooldownManager().setLastUse(id, System.currentTimeMillis());
    if (addon.configuration().getQolConfig().getCoolDown().get()) {
      functionLink.setCoolDown(e.getItemStack(), (int) ((cooldownTime/1000)*20) );
    }
  }

}
