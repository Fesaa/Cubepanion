package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.accessors.CCItemStack;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import art.ameliah.laby.addons.cubepanion.core.events.GameJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.listener.internal.SessionTracker;
import art.ameliah.laby.addons.cubepanion.core.utils.AutoVoteProvider;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.FunctionLink;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.VotingLink.VotePair;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.entity.player.inventory.InventorySetSlotEvent;
import net.labymod.api.util.concurrent.task.Task;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutoVote {

  private final Logging log = Logging.create(Cubepanion.class.getSimpleName());
  private final Pattern votePattern = Pattern.compile(
      "(?:.{0,5} |)([a-zA-Z0-9_]{2,16})(?: .{0,5}|) voted for [a-zA-Z ]+\\. \\d+ votes?");
  private final Pattern startingInPattern = Pattern.compile(
      "[a-zA-Z ]+ is starting in 10 seconds.");

  private final Cubepanion addon;

  @NotNull
  private final FunctionLink functionLink;
  private int returnIndex = 31;

  private boolean hasVoted;
  private boolean caughtVotingItem;
  private boolean voteHasBeenConfirmed;

  public AutoVote(Cubepanion addon, @NotNull FunctionLink functionLink) {
    this.addon = addon;
    this.functionLink = functionLink;
  }

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {
    if (!this.addon.getManager().onCubeCraft()) {
      return;
    }

    if (!this.addon.getManager().isInPreLobby()) {
      return;
    }

    var matcher = votePattern.matcher(e.chatMessage().getPlainText());
    if (matcher.matches()) {
      var player = matcher.group(1);
      if (SessionTracker.get().username().equals(player)) {
        this.voteHasBeenConfirmed = true;
      }
      return;
    }

    if (this.voteHasBeenConfirmed) {
      return;
    }

    var startingMatcher = startingInPattern.matcher(e.chatMessage().getPlainText());
    if (startingMatcher.matches()) {
      AutoVoteSubConfig config = this.addon.configuration().getAutoVoteSubConfig();
      if (!config.isEnabled()) {
        return;
      }

      var game = this.addon.getManager().getDivision();
      AutoVoteProvider provider = AutoVoteProvider.getProvider(game);
      if (provider == null) {
        return;
      }

      log.debug("Voting for game {} start as the previous attempt has failed", game);
      this.vote(game, provider);
    }
  }

  @Subscribe
  public void onSetSlot(InventorySetSlotEvent event) {
    if (this.hasVoted) {
      return;
    }

    if (!this.addon.getManager().isInPreLobby()) {
      return;
    }

    if (!this.isVotingItem(event.itemStack())) {
      return;
    }

    AutoVoteSubConfig config = this.addon.configuration().getAutoVoteSubConfig();
    if (!config.isEnabled()) {
      return;
    }

    var game = this.addon.getManager().getDivision();
    AutoVoteProvider provider = AutoVoteProvider.getProvider(game);
    if (provider == null) {
      this.caughtVotingItem = true;
      log.warn("Voting item found but division {} has no registered voting options", game);
      return;
    }

    this.hasVoted = true;
    this.vote(game, provider);
  }

  @Subscribe
  public void onGameJoin(GameJoinEvent e) {
    if (!this.caughtVotingItem) {
      this.reset();
      return;
    }

    this.caughtVotingItem = false;
    log.debug("Voting item found, but before game joining. Trying to vote now");
    AutoVoteSubConfig config = this.addon.configuration().getAutoVoteSubConfig();
    if (!config.isEnabled()) {
      return;
    }

    var game = this.addon.getManager().getDivision();
    AutoVoteProvider provider = AutoVoteProvider.getProvider(game);
    if (provider == null) {
      log.warn(
          "Voting item found but division {} has no registered voting options, even after game join",
          game);
      return;
    }

    this.vote(game, provider);
  }

  private void reset() {
    this.hasVoted = false;
    this.voteHasBeenConfirmed = false;
    this.caughtVotingItem = false;
  }

  private boolean isVotingItem(ItemStack itemStack) {
    Component component = itemStack.getDisplayName();
    List<Component> children = component.getChildren();
    if (children.size() != 1) {
      return false;
    }
    children = children.getFirst().getChildren();
    if (children.size() != 1) {
      return false;
    }

    component = children.getFirst();
    if (!(component instanceof TextComponent displayName)) {
      return false;
    }

    return displayName.getText().equals("Voting");
  }

  private void vote(CubeGame game, AutoVoteProvider provider) {
    int delay = Cubepanion.get().configuration().getAutoVoteSubConfig().getDelay().get();
    log.debug("Starting vote sequence for hotbar slot {} with a delay of {}ms",
        provider.getHotbarSlot(), delay);

    if (delay == 0) {
      this.startVoteSequence(game, provider);
      return;
    }
    Task.builder(() -> this.startVoteSequence(game, provider)).delay(delay, TimeUnit.MILLISECONDS)
        .build().execute();
  }

  private void startVoteSequence(CubeGame game, AutoVoteProvider provider) {
    log.info("Going to vote for {}", game);

    this.functionLink.useItemInHotBar(provider.getHotbarSlot());
    Laby.labyAPI().minecraft().executeNextTick(() -> this.menuLogic(provider));
  }

  private void menuLogic(AutoVoteProvider provider) {
    Queue<VotePair> pairs = new LinkedList<>();
    provider.getVotePairSuppliers().forEach(pair -> pairs.add(pair.get()));

    this.clickLoop(pairs, null).thenAcceptAsync(lastPair -> this.waitForNextMenu(lastPair)
            .thenAcceptAsync(ctx -> this.functionLink.clickSlot(this.returnIndex, 0))
        .exceptionally(ex -> {
          log.error("Failed to vote", ex);
          return null;
        }));
  }

  private CompletableFuture<VotePair> clickLoop(Queue<VotePair> pairs, VotePair lastPair) {
    var next = pairs.poll();
    if (next == null) {
      return CompletableFuture.completedFuture(lastPair);
    }
    return this.handleVotePair(next)
        .thenComposeAsync(_lastPair -> this.clickLoop(pairs, _lastPair));
  }

  private CompletableFuture<VotePair> handleVotePair(VotePair pair) {
    if (!pair.valid()) {
      return CompletableFuture.completedFuture(pair);
    }

    log.debug("Handling vote pair {}", pair);

    return this.waitForNextMenu(pair).thenAcceptAsync(items -> {
      if (items == null) {
        throw new IllegalStateException("Failed to open the next menu");
      }
      if (pair.choiceIndex() == -1) {
        return;
      }

      log.debug("Clicking choice {} @ {}", pair.choiceIndex(), items.get(pair.choiceIndex()).getDisplayName());
      this.functionLink.clickSlot(pair.choiceIndex(), 0);
    }).thenComposeAsync(ignored -> this.waitForNextVotingMenu(pair)
        .thenApplyAsync(items -> {
          if (items == null) {
            throw new IllegalStateException("Voting menu failed to open");
          }

          log.debug("Clicking vote {} @ {}", pair.voteIndex(), items.get(pair.voteIndex()).getDisplayName());
          this.functionLink.clickSlot(pair.voteIndex(), 0);
          this.functionLink.clickSlot(this.returnIndex, 0);
          return pair;
        })
    );
  }

  private CompletableFuture<@Nullable List<CCItemStack>> waitForNextMenu(VotePair pair) {
    return this.functionLink.loadMenuItems(
        (title) -> title.toLowerCase().contains("voting") && !title.toLowerCase().contains(pair.menuTitle()),
        (items) -> items.size() >= 70);
  }

  private CompletableFuture<@Nullable List<CCItemStack>> waitForNextVotingMenu(VotePair pair) {
    return this.functionLink.loadMenuItems(
        (title) -> title.toLowerCase().contains(pair.menuTitle()),
        (items) -> items.size() >= 70);
  }


}
