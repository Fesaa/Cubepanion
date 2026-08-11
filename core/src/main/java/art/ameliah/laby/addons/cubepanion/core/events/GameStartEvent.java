package art.ameliah.laby.addons.cubepanion.core.events;

import art.ameliah.laby.addons.cubepanion.core.external.Game;
import net.labymod.api.event.Event;

public record GameStartEvent(Game game) implements Event {

}
