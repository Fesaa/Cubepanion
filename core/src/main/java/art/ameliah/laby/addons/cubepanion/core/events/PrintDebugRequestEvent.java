package art.ameliah.laby.addons.cubepanion.core.events;

import net.labymod.api.event.Event;

public record PrintDebugRequestEvent(Receiver receiver) implements Event {

  public enum Receiver {
    AutoGG,
    ScoreboardListener,
    Cooldowns,
  }

}
