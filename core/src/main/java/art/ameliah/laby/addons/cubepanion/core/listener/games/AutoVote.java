package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import art.ameliah.laby.addons.cubepanion.core.events.GameJoinEvent;
import art.ameliah.laby.addons.cubepanion.core.utils.AutoVoteProvider;
import art.ameliah.laby.addons.cubepanion.core.utils.LOGGER;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.VotingLink;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.entity.player.inventory.InventorySetSlotEvent;
import java.util.List;

public class AutoVote {

  private final Cubepanion addon;
  private boolean hasVoted;

  public AutoVote(Cubepanion addon) {
    this.addon = addon;
  }

  // I don't know how to properly work with component. Please forgive me
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

    VotingLink link = Cubepanion.get().getVotingLink();
    AutoVoteProvider provider = AutoVoteProvider.getProvider(this.addon.getManager().getDivision());
    if (link != null && provider != null) {
      this.hasVoted = true;
      link.vote(provider);
    }
  }

  @Subscribe
  public void onGameJoin(GameJoinEvent e) {
    this.hasVoted = false;
  }

}
