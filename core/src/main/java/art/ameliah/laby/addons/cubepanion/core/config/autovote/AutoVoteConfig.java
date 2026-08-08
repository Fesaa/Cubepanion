package art.ameliah.laby.addons.cubepanion.core.config.autovote;

import net.labymod.api.configuration.loader.Config;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class AutoVoteConfig extends Config {

  private Map<String, Integer> selectedAutoVoteSlots = new HashMap<>();

  @NotNull
  public Map<String, Integer> getSlots() {
    return selectedAutoVoteSlots;
  }

}
