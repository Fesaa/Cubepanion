package art.ameliah.laby.addons.cubepanion.core.external.autovote;

import java.util.List;

public record AutoVoteCategory(String id, String name, String itemId, int choiceIndex, String menuTitle, List<AutoVoteCategoryOption> options) {

}
