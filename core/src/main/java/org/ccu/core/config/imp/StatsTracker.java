package org.ccu.core.config.imp;

import java.util.Calendar;
import java.util.HashMap;

public class StatsTracker {

  private int Daily;
  private int DailyMax;
  private int AllTimeDailyMax;
  private int AllTime;
  private int AllTimeMax;
  private final HashMap<String, StatsTracker> historicalData;

  public StatsTracker() {
    this.Daily = 0;
    this.DailyMax = 0;
    this.AllTimeDailyMax = 0;
    this.AllTime = 0;
    this.AllTimeMax = 0;
    this.historicalData = new HashMap<>();
  }

  private StatsTracker(int Daily, int DailyMax, int AllTimeDailyMax, int AllTime, int AllTimeMax) {
    this.Daily = Daily;
    this.DailyMax = DailyMax;
    this.AllTimeDailyMax = AllTimeDailyMax;
    this.AllTime = AllTime;
    this.AllTimeMax = AllTimeMax;
    this.historicalData = new HashMap<>();
  }

  private StatsTracker Copy() {
    return new StatsTracker(this.Daily, this.DailyMax, this.AllTimeDailyMax, this.AllTime, this.AllTimeMax);
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

  public HashMap<String, StatsTracker> getHistoricalData() {
    return this.historicalData;
  }

  public StatsTracker getHistoricalData(String date) {
    return this.historicalData.get(date);
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

  public void registerFail() {
    this.Daily = 0;
    this.AllTime = 0;
  }

  public void registerNewDay() {
    this.Daily = 0;
    this.DailyMax = 0;
    this.historicalData.put(this.getDate(), this.Copy());
  }

  private String getDate() {
    Calendar cal = Calendar.getInstance();
    return cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
  }

}
