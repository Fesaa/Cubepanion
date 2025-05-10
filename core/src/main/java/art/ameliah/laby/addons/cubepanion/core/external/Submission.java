package art.ameliah.laby.addons.cubepanion.core.external;

import java.util.List;

public record Submission(String uuid, int gameId, List<LeaderboardRow> entries) {

}
