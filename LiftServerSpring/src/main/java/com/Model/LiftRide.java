package com.Model;

public class LiftRide {
  private Integer time;
  private Integer liftId;

  public LiftRide(Integer time, Integer liftId) {
    this.time = time;
    this.liftId = liftId;
  }

  public Integer getTime() {
    return time;
  }

  public Integer getLiftId() {
    return liftId;
  }

  public void setTime(Integer time) {
    this.time = time;
  }

  public void setLiftId(Integer liftId) {
    this.liftId = liftId;
  }
}