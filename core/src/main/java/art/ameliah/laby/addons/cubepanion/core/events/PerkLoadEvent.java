package art.ameliah.laby.addons.cubepanion.core.events;

import net.labymod.api.client.world.item.Item;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Event;

public class PerkLoadEvent implements Event {

  private final PerkCategory category;

  private final ItemStack[] perks;

  public PerkLoadEvent(PerkCategory category, ItemStack... perks) {
    this.category = category;
    this.perks = perks;
  }

  public PerkCategory getCategory() {
    return category;
  }

  public ItemStack[] getPerks() {
    return perks;
  }


  public enum PerkCategory {
    PERSONAL,
    TEAM,
    GAME
  }

}
