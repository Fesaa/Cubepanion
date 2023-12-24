package org.cubepanion.core.listener.games;

import java.util.Collection;
import java.util.List;
import net.labymod.api.event.Subscribe;
import org.cubepanion.core.Cubepanion;
import org.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import org.cubepanion.core.events.GameUpdateEvent;
import org.cubepanion.core.utils.CubeGame;
import org.cubepanion.core.versionlinkers.VotingLink;

public class AutoVote {

  private static final Collection<CubeGame> autoVote = List.of(
      CubeGame.TEAM_EGGWARS,
      CubeGame.SOLO_SKYWARS,
      CubeGame.SOLO_LUCKYISLANDS
  );

  @Subscribe
  public void onGameJoin(GameUpdateEvent e) {
    if (!e.isPreLobby()) {
      return;
    }

    if (!autoVote.contains(e.getDestination())) {
      return;
    }

    AutoVoteSubConfig config = Cubepanion.get().configuration().getAutoVoteSubConfig();
    if (!config.isEnabled()) {
      return;
    }

    VotingLink link = Cubepanion.get().getVotingLink();
    if (link != null) {
      link.vote(e.getDestination(), config);
    }
  }

}
