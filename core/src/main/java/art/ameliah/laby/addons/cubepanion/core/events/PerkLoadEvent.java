package art.ameliah.laby.addons.cubepanion.core.events;

import java.util.List;
import art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Event;
import org.jetbrains.annotations.NotNull;
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
    PERSONAL("difficulty_easy", art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory.PERSONAL),
    TEAM("difficulty_medium", art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory.TEAM),
    GAME("difficulty_plus", art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory.GAME);

    private final String CubeTapItemVariant;
    private final art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory protoCategory;

    PerkCategory(String CubeTapItemVariant, art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory protoCategory) {
      this.CubeTapItemVariant = CubeTapItemVariant;
      this.protoCategory = protoCategory;
    }

    public art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory getProtoCategory() {
      return protoCategory;
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

    @NotNull
    public static PerkCategory fromProtoCategory(art.ameliah.laby.addons.cubepanion.core.proto.PerkCategory protoCategory) {
      for (PerkCategory category : values()) {
        if (category.protoCategory == protoCategory) {
          return category;
        }
      }
      throw new IllegalArgumentException("No PerkCategory found for proto category " + protoCategory);
    }
  }

}
