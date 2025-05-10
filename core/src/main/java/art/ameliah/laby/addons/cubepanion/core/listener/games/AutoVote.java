package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import art.ameliah.laby.addons.cubepanion.core.events.GameJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.listener.internal.SessionTracker;
import art.ameliah.laby.addons.cubepanion.core.utils.AutoVoteProvider;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.VotingLink;
import java.util.List;
import java.util.regex.Pattern;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import net.labymod.api.event.client.entity.player.inventory.InventorySetSlotEvent;
import net.labymod.api.util.logging.Logging;
import org.jetbrains.annotations.NotNull;

public class AutoVote {

  private final Logging log = Logging.create(Cubepanion.class.getSimpleName());
  private final Pattern votePattern = Pattern.compile("(?:.{0,5} |)([a-zA-Z0-9_]{2,16})(?: .{0,5}|) voted for [a-zA-Z ]+\\. \\d+ votes?");
  private final Pattern startingInPattern = Pattern.compile("[a-zA-Z ]+ is starting in 10 seconds.");

  private final Cubepanion addon;
  @NotNull
  private final VotingLink votingLink;
  private boolean hasVoted;
  private boolean caughtVotingItem;
  private boolean voteHasBeenConfirmed;

  public AutoVote(Cubepanion addon, @NotNull VotingLink votingLink) {
    this.addon = addon;
    this.votingLink = votingLink;
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

      AutoVoteProvider provider = AutoVoteProvider.getProvider(this.addon.getManager().getDivision());
      if (provider == null) {
        return;
      }

      log.debug("Voting for game {} start as the previous attempt has failed", this.addon.getManager().getDivision());
      this.votingLink.vote(provider);
    }
  }

  @Subscribe
  public void onSetSlot(InventorySetSlotEvent event) {
    if (this.hasVoted) {
      return;
    }

    Component component = event.itemStack().getDisplayName();
    List<Component> children = component.getChildren();
    if (children.size() != 1) {
      return;
    }
    children = children.getFirst().getChildren();
    if (children.size() != 1) {
      return;
    }

    component = children.getFirst();
    if (!(component instanceof TextComponent displayName)) {
      return;
    }

    if (!displayName.getText().equals("Voting")) {
      return;
    }

    AutoVoteSubConfig config = this.addon.configuration().getAutoVoteSubConfig();
    if (!config.isEnabled()) {
      return;
    }

    AutoVoteProvider provider = AutoVoteProvider.getProvider(this.addon.getManager().getDivision());
    if (provider == null) {
      this.caughtVotingItem = true;
      log.warn("Voting item found but division {} has no registered voting options", this.addon.getManager().getDivision());
      return;
    }

    this.hasVoted = true;
    this.votingLink.vote(provider);
  }

  @Subscribe
  public void onGameJoin(GameJoinEvent e) {
    if (!this.caughtVotingItem) {
      this.hasVoted = false;
      this.voteHasBeenConfirmed = false;
      return;
    }

    this.caughtVotingItem = false;
    log.info("Voting item found, but before game joining. Trying to vote now");
    AutoVoteSubConfig config = this.addon.configuration().getAutoVoteSubConfig();
    if (!config.isEnabled()) {
      return;
    }

    AutoVoteProvider provider = AutoVoteProvider.getProvider(this.addon.getManager().getDivision());
    if (provider == null) {
      log.warn("Voting item found but division {} has no registered voting options, even after game join", this.addon.getManager().getDivision());
      return;
    }

    this.votingLink.vote(provider);
  }

}
