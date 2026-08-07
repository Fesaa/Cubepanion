package art.ameliah.laby.addons.cubepanion.core.external.autovote;

import java.util.List;

public record AutoVoteConfig(int gameId, int hotbarSlot, List<AutoVoteCategory> categories) {

}
