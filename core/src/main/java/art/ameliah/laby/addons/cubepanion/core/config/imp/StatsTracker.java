package art.ameliah.laby.addons.cubepanion.core.config.imp;

public class StatsTracker {

  private int Daily;
  private int DailyMax;
  private int AllTimeDailyMax;
  private int AllTime;
  private int AllTimeMax;

  public StatsTracker() {
    this.Daily = 0;
    this.DailyMax = 0;
    this.AllTimeDailyMax = 0;
    this.AllTime = 0;
    this.AllTimeMax = 0;
  }

  public int getAllTime() {
    return this.AllTime;
  }

  public int getAllTimeDailyMax() {
    return this.AllTimeDailyMax;
  }

  public int getAllTimeMax() {
    return this.AllTimeMax;
  }

  public int getDaily() {
    return this.Daily;
  }

  public int getDailyMax() {
    return this.DailyMax;
  }

  public void registerSuccess() {
    this.Daily++;
    if (this.Daily > this.DailyMax) {
      this.DailyMax = Daily;
    }
    if (this.DailyMax > this.AllTimeDailyMax) {
      this.AllTimeDailyMax = this.DailyMax;
    }

    this.AllTime++;
    if (this.AllTime > this.AllTimeMax) {
      this.AllTimeMax = this.AllTime;
    }
  }

  public void registerSuccess(int val) {
    this.Daily += val;
    if (this.Daily > this.DailyMax) {
      this.DailyMax = Daily;
    }
    if (this.DailyMax > this.AllTimeDailyMax) {
      this.AllTimeDailyMax = this.DailyMax;
    }

    this.AllTime += val;
    if (this.AllTime > this.AllTimeMax) {
      this.AllTimeMax = this.AllTime;
    }
  }

  public void registerFail() {
    this.Daily = 0;
    this.AllTime = 0;
  }

  public void registerNewDay() {
    this.Daily = 0;
    this.DailyMax = 0;
  }

}
