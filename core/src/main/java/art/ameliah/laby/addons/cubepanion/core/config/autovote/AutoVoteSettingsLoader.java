package art.ameliah.laby.addons.cubepanion.core.config.autovote;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.external.Game;
import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteConfiguration;
import art.ameliah.laby.addons.cubepanion.core.listener.games.AutoVote.VotePair;
import art.ameliah.laby.addons.cubepanion.core.utils.AutoVoteProvider;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.configuration.settings.type.SettingElement;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class AutoVoteSettingsLoader {

  private final Logging log = Logging.create(Cubepanion.class.getSimpleName());
  private static final Duration RETRY_DELAY = Duration.ofSeconds(2);

  private final Task retryTask = Task.builder(() -> loadAndRegisterSettings(false))
      .delay(2, TimeUnit.MINUTES)
      .build();

  private final List<AutoVoteConfiguration> configs = new ArrayList<>();

  private final AutoVoteConfig config;

  public AutoVoteSettingsLoader(Cubepanion addon) {
    this.config = addon.configuration().getAutoVoteConfig();
  }

  public void loadAndRegisterSettings(boolean retryWithBackOff) {
    var settings = Laby.labyAPI().coreSettingRegistry().getById("cubepanion")
        .getById("autoVoteConfig");
    if (settings == null) {
      log.warn("AutoVote config not found, cannot register dynamic auto vote options");
      return;
    }

    CubepanionAPI.I().getAutoVoteConfig()
        .exceptionallyComposeAsync(ex -> {
          log.warn("Initial load failed, retrying in {}", RETRY_DELAY, ex);
          return CompletableFuture
              .supplyAsync(() -> null, CompletableFuture.delayedExecutor(
                  RETRY_DELAY.toMillis(), TimeUnit.MILLISECONDS))
              .thenComposeAsync(ignored -> CubepanionAPI.I().getAutoVoteConfig());
        }).handleAsync((configs, ex) -> {
          if (ex != null) {
            log.warn("Failed to load auto vote config. With retry again in 2m");

            // TODO: Show toast

            if (retryWithBackOff) {
              retryTask.execute();
            }
            return null;
          }

          log.info("Registering settings for {} games", configs.size());

          this.configs.addAll(configs);

          Laby.labyAPI().minecraft().executeOnRenderThread(() -> {
            for (var config : configs) {
              Icon icon = null;
              if (config.icon() != null && !config.icon().isEmpty()) {
                icon = Icon.url(config.icon());
              }
              settings.register(new AutoVoteGameElement(this.config, config, icon));
            }
          });

          return null;
        }).exceptionally(ex -> {
          log.error("An error occurred while creating auto vote configuration", ex);
          return null;
        });
  }

  @Nullable
  public AutoVoteProvider getProvider(Game game) {
    log.debug("Getting vote provider for {}", game);
    for (var config : configs) {
      if (config.gameId() == game.id()) {
        return buildProviderForConfig(config);
      }
    }

    return null;
  }

  @Nullable
  private AutoVoteProvider buildProviderForConfig(AutoVoteConfiguration configuration) {
    var votePairs = new ArrayList<Supplier<VotePair>>();

    for (var category : configuration.categories()) {
      var slot = config.getSlots().get(category.id());
      if (slot == null) {
        return null;
      }

      votePairs.add(() -> VotePair.of(category.choiceIndex(), slot, category.menuTitle()));
    }

    return AutoVoteProvider.of(configuration.hotbarSlot(), votePairs);
  }

}
