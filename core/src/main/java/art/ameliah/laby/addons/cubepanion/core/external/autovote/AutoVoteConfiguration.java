package art.ameliah.laby.addons.cubepanion.core.external.autovote;

import java.util.List;

public record AutoVoteConfiguration(int gameId, String gameName, int hotbarSlot, String icon, List<AutoVoteCategory> categories) {

}
