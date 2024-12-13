package art.ameliah.laby.addons.cubepanion.core.events;

import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import net.labymod.api.event.Event;

public record GameStartEvent(CubeGame game) implements Event {

}
