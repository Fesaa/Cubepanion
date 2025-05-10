package art.ameliah.laby.addons.cubepanion.core.accessors;

import java.util.List;
import net.labymod.api.client.world.item.ItemStack;

public interface CCItemStack extends ItemStack {

  List<String> getToolTips();

  CCCompoundTag getCustomDataTag();

  String texture();

}
