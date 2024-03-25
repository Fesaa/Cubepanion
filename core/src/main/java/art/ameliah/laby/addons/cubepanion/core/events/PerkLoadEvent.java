package art.ameliah.laby.addons.cubepanion.core.events;

import java.util.List;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Event;
import org.jetbrains.annotations.Nullable;

public class PerkLoadEvent implements Event {

  private final PerkCategory category;

  private final ItemStack[] perks;

  private boolean fromWS = false;

  public PerkLoadEvent(PerkCategory category, ItemStack... perks) {
    this.category = category;
    this.perks = perks;
  }

  public PerkLoadEvent(PerkCategory category, List<ItemStack> perks) {
    this.category = category;
    this.perks = perks.toArray(new ItemStack[0]);

  }

  public PerkLoadEvent(PerkCategory category, List<ItemStack> perks, boolean fromWS) {
    this.category = category;
    this.perks = perks.toArray(new ItemStack[0]);
    this.fromWS = fromWS;
  }

  public PerkCategory getCategory() {
    return category;
  }

  public ItemStack[] getPerks() {
    return perks;
  }

  public boolean fromWS() {
    return fromWS;
  }


  public enum PerkCategory {
    PERSONAL("difficulty_easy"),
    TEAM("difficulty_medium"),
    GAME("difficulty_plus");

    private final String CubeTapItemVariant;

    PerkCategory(String CubeTapItemVariant) {
      this.CubeTapItemVariant = CubeTapItemVariant;
    }

    public String getCubeTapItemVariant() {
      return CubeTapItemVariant;
    }

    @Nullable
    public static PerkCategory fromCubeTapItemVariant(String CubeTapItemVariant) {
      for (PerkCategory category : values()) {
        if (category.CubeTapItemVariant.equals(CubeTapItemVariant)) {
          return category;
        }
      }
      return null;
    }
  }

}
