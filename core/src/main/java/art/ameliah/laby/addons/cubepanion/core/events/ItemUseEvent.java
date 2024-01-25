package art.ameliah.laby.addons.cubepanion.core.events;

import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Event;

/**
 * Attack destroy is always called with UseType = SWING Use Item / Place Block is only called if
 * ItemStack isn't air
 */
public class ItemUseEvent implements Event {

  private final UseType useType;

  private final Hand hand;

  private final ItemStack itemStack;

  public ItemUseEvent(UseType useType, ItemStack itemStack, Hand hand) {
    this.useType = useType;
    this.itemStack = itemStack;
    this.hand = hand;
  }


  public UseType getUseType() {
    return useType;
  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public Hand getHand() {
    return hand;
  }


  public enum UseType {
    USE, SWING;
  }

  public enum Hand {
    MAIN_HAND, OFF_HAND;
  }

}
