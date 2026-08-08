package art.ameliah.laby.addons.cubepanion.core.config.autovote;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.external.CubepanionAPI;
import art.ameliah.laby.addons.cubepanion.core.external.Game;
import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteCategory;
import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteCategoryOption;
import art.ameliah.laby.addons.cubepanion.core.external.autovote.AutoVoteConfiguration;
import art.ameliah.laby.addons.cubepanion.core.listener.games.AutoVote.VotePair;
import art.ameliah.laby.addons.cubepanion.core.utils.AutoVoteProvider;
import net.labymod.api.Laby;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.CategoryWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.configuration.settings.type.SettingElement;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.Nullable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
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
    var settings = Laby.labyAPI().coreSettingRegistry().getById("cubepanion").getById("autoVoteConfig");
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
            var allElements = new ArrayList<SettingElement>();

            for (var config: configs) {
              allElements.addAll(createSettingElements(config));
            }

            settings.register(allElements);
          });

          return null;
        }).exceptionally(ex -> {
          log.error("An error occurred while creating auto vote configuration", ex);
          return null;
        });
  }

  private List<SettingElement> createSettingElements(
      AutoVoteConfiguration config) {
    var elements = new ArrayList<SettingElement>(config.categories().size());

    for (var category : config.categories()) {
      /*ResourceLocation resourceLocation;
      if (category.itemId() != null && !category.itemId().isEmpty()) {
       resourceLocation = ResourceLocation.create("minecraft", category.itemId());
      } else {
        resourceLocation = ResourceLocation.create("minecraft", "bundle");
      }*/

      var element = new SettingElement(category.id(), null, category.name(), new String[] {
          category.name()
      });

      element.setWidgets(new Widget[] {
          new AutoVoteCategoryDropdownWidget(this.config, category)
      });

      elements.add(element);
    }

    log.debug("Created {} setting elements for {}", elements.size(), config.gameId());
    return elements;
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
