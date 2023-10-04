package org.cubepanion.core.weave;


import java.net.MalformedURLException;
import java.net.URL;

/**
 * API access class
 */
public class Weave {

  private final LeaderboardAPI leaderboardAPI;
  private final ChestAPI chestAPI;
  private final EggWarsMapAPI eggWarsMapAPI;

  private Weave(String domain, int port, boolean ssl) throws MalformedURLException {
    String baseURL = (new URL(
        String.format("http%s://%s:%d", ssl ? "s" : "", domain, port))).toString();
    leaderboardAPI = new LeaderboardAPI(baseURL);
    chestAPI = new ChestAPI(baseURL);
    eggWarsMapAPI = new EggWarsMapAPI(baseURL);
  }

  private Weave(String domain, boolean ssl) throws MalformedURLException {
    String baseURL = (new URL(String.format("http%s://%s", ssl ? "s" : "", domain))).toString();
    leaderboardAPI = new LeaderboardAPI(baseURL);
    chestAPI = new ChestAPI(baseURL);
    eggWarsMapAPI = new EggWarsMapAPI(baseURL);
  }

  /**
   * Setup API in prod env
   *
   * @return Weave
   */
  public static Weave Production() {
    try {
      return new Weave("ameliah.art/cubepanion_api", true);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Setup API for the default dev env (http://127.0.0.1:8080)
   *
   * @param ssl if ssl is enabled
   * @return Weave
   */
  public static Weave Dev(boolean ssl) {
    try {
      return new Weave("127.0.0.1", 8080, ssl);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Setup API in dev env with custom port
   *
   * @param port custom port
   * @param ssl  if ssl is enabled
   * @return Weave
   * @throws MalformedURLException Could not construct API-url
   */
  public static Weave Dev(int port, boolean ssl) throws MalformedURLException {
    return new Weave("127.0.0.1", port, ssl);
  }

  /**
   * Setup custom API in dev env
   *
   * @param domain custom domain
   * @param port   custom port
   * @param ssl    if ssl is enabled
   * @return Weave
   * @throws MalformedURLException Could not construct API-url
   */
  public static Weave Dev(String domain, int port, boolean ssl) throws MalformedURLException {
    return new Weave(domain, port, ssl);
  }

  /**
   * @return LeaderboardAPI
   */
  public LeaderboardAPI getLeaderboardAPI() {
    return leaderboardAPI;
  }

  /**
   * @return ChestAPI
   */
  public ChestAPI getChestAPI() {
    return chestAPI;
  }

  /**
   * @return EggWarsMapAPI
   */
  public EggWarsMapAPI getEggWarsMapAPI() {
    return eggWarsMapAPI;
  }
}
