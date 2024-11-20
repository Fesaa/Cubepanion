package art.ameliah.laby.addons.cubepanion.core.weave;

import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import com.google.gson.reflect.TypeToken;
import javax.inject.Singleton;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static art.ameliah.laby.addons.cubepanion.core.weave.Utils.makeRequest;

@Singleton
public class GamesAPI {

  private static final String baseURL = System.getenv("CUBEPANION_DEV") != null
      ? "http://127.0.0.1/cubepanion/games"
      : "https://ameliah.art/cubepanion/games";

  private static GamesAPI instance;

  private final Type autoVoteMapTypeToken = new TypeToken<HashMap<String, AutoVoteConfig>>() {}.getType();
  private Map<String, AutoVoteConfig> configs = new HashMap<>();

  private GamesAPI() {
    if (instance != null) {
      throw new IllegalStateException("Already instantiated");
    }
    instance = this;
  }

  public static GamesAPI I() {
    return instance;
  }

  public static void Init() {
    instance = new GamesAPI();
    instance.load();
  }

  private void load() {
    makeRequest(baseURL + "/autoVote/config", autoVoteMapTypeToken)
        .handleAsync((configs, throwable) -> {
          if (throwable != null) {
            LOGGER.error(getClass(), "Failed to load autovote config", throwable);
            return null;
          }

          this.configs = (Map<String, AutoVoteConfig>) configs;
          LOGGER.debug(getClass(), "Loaded ", this.configs.size(), " auto vote configs");
          return null;
        });
  }

  public record AutoVoteConfig(int hotbar_index, List<AutoVoteOption> options) {}

  public record AutoVoteOption(String game, String choice_translation, int choice_index, AutoVoteType type, String[] translations) {}

  public enum AutoVoteType {
    TwoOptions,
    ThreeOptions,
    FourOptions,
    FiveOptions;
  }


}
