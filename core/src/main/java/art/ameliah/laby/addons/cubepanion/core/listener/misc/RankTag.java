package art.ameliah.laby.addons.cubepanion.core.listener.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.labymod.api.Laby;
import net.labymod.api.client.Minecraft;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import art.ameliah.laby.addons.cubepanion.core.Cubepanion;
import art.ameliah.laby.addons.cubepanion.core.events.RequestEvent;

public class RankTag {

  private final Pattern whoList = Pattern.compile(
      "[:|,] (?<rankstring>.) (?:.{0,5} |)(?<username>[a-zA-Z0-9_]{2,16})(?: .{0,5}|)");

  private boolean hasRequested = false;

  @Subscribe
  public void onChatMessage(ChatReceiveEvent e) {

    Minecraft mc = Laby.labyAPI().minecraft();
    ClientPlayer p = mc.getClientPlayer();
    if (p == null) {
      return;
    }
    String msg = e.chatMessage().getPlainText();

    if (hasRequested) {
      Matcher whoListMatcher = this.whoList.matcher(msg);
      while (whoListMatcher.find()) {
        String username = whoListMatcher.group("username");
        String rankString = whoListMatcher.group("rankstring");
        if (username.equals(p.getName())) {
          Cubepanion.get().getManager().setRankString(rankString);
          hasRequested = false;
          e.setCancelled(true);
          break;
        }
      }
    }
  }

  @Subscribe
  public void onRequest(RequestEvent e) {
    if (e.getType().equals(RequestEvent.RequestType.RANK_TAG)) {
      hasRequested = true;
    }
  }

}
