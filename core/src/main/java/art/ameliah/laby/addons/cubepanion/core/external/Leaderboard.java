package art.ameliah.laby.addons.cubepanion.core.external;

import java.util.Date;
import java.util.List;

public record Leaderboard(int gameId, Date lastUpdated, List<LeaderboardRow> rows) {

}
