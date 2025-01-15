package art.ameliah.laby.addons.cubepanion.core.listener;

import art.ameliah.laby.addons.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import com.google.gson.JsonObject;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.config.ConfigurationVersionUpdateEvent;

public class ConfigFixes {


  @Subscribe
  public void onConfigVersionUpdate(ConfigurationVersionUpdateEvent event) {
    Class<? extends Config> configClass = event.getConfigClass();
    int configVersion = event.getUsedVersion();

    // Hard coded nonsense, should be all fine
    if (configClass == AutoVoteSubConfig.class) {
      if (configVersion == 1) {
        JsonObject obj = event.getJsonObject();

        if (obj.has("eggWarsHealth")) {
          obj.addProperty("eggWarsHealth", 0);
        }

        event.setJsonObject(obj);
      }
    }
  }

}
