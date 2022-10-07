package Part2.Model;

import Part2.DataGeneration.SkiersGenerator;
import io.swagger.client.model.LiftRide;

/**
 * SkiersWrapper is a POJO/wrapper object that generates data on instantiation
 */
public class SkiersWrapper {

  private LiftRide liftRide;

  private Integer resortID;

  private String seasonID;

  private String day;

  private Integer skierId;

  public SkiersWrapper() {
    SkiersGenerator generator = new SkiersGenerator();
    this.liftRide = generator.jsonGenerator();
    this.resortID = generator.generateResortID();
    this.seasonID = generator.generateSeasonID();
    this.day = generator.getDay();
    this.skierId = generator.generateSkierID();
  }

  public LiftRide getLiftRide() {
    return liftRide;
  }

  public Integer getResortID() {
    return resortID;
  }

  public String getSeasonID() {
    return seasonID;
  }

  public String getDay() {
    return day;
  }

  public Integer getSkierId() {
    return skierId;
  }

  @Override
  public String toString() {
    return "SkiersWrapper{" +
        "liftRide=" + liftRide +
        ", resortID=" + resortID +
        ", seasonID='" + seasonID + '\'' +
        ", day='" + day + '\'' +
        ", skierId=" + skierId +
        '}';
  }

}
