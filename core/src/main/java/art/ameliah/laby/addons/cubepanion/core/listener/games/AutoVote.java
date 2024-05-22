package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import art.ameliah.laby.addons.cubepanion.core.events.GameUpdateEvent;
import art.ameliah.laby.addons.cubepanion.core.utils.AutoVoteProvider;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.VotingLink;
import net.labymod.api.event.Subscribe;

public class AutoVote {

  @Subscribe
  public void onGameJoin(GameUpdateEvent e) {
    if (!e.isPreLobby()) {
      return;
    }

    AutoVoteSubConfig config = Cubepanion.get().configuration().getAutoVoteSubConfig();
    if (!config.isEnabled()) {
      return;
    }

    VotingLink link = Cubepanion.get().getVotingLink();
    AutoVoteProvider provider = AutoVoteProvider.getProvider(e.getDestination());
    if (link != null && provider != null) {
      link.vote(provider);
    }
  }

}
