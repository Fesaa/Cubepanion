package art.ameliah.laby.addons.cubepanion.core.listener.games;

import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.config.subconfig.AutoVoteSubConfig;
import art.ameliah.laby.addons.cubepanion.core.events.GameUpdateEvent;
import art.ameliah.laby.addons.cubepanion.core.utils.CubeGame;
import art.ameliah.laby.addons.cubepanion.core.versionlinkers.VotingLink;
import java.util.Collection;
import java.util.List;
import net.labymod.api.event.Subscribe;

public class AutoVote {

  private static final Collection<CubeGame> autoVote = List.of(
      CubeGame.TEAM_EGGWARS,
      CubeGame.SOLO_SKYWARS,
      CubeGame.SOLO_LUCKYISLANDS,
      CubeGame.PILLARS_OF_FORTUNE
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
