package Part1.DataGeneration;

import io.swagger.client.model.LiftRide;

// TODO: create a wrapper
public class SkiersGenerator extends AbstractGenerator implements IRandomGenerator {

  protected static final int DAY = 1;

  public SkiersGenerator() {
  }

//  @Override
//  public String endpointGenerator() {
//    int skierID = AbstractGenerator.idGenerated(100000);
//    int resortID = AbstractGenerator.idGenerated(10);
//
//    // /123/seasons/123/days/123/skiers/123
//    return "/" + String.valueOf(resortID) + "/seasons/" + String.valueOf(LocalDate.now().getYear())
//                      + "/days/" + String.valueOf(DAY) + "/skiers/" + String.valueOf(skierID);
//  }

  @Override
  public LiftRide jsonGenerator() {
    Integer liftID = AbstractGenerator.idGenerated(40);
    Integer time = AbstractGenerator.idGenerated(360);
//    "{ \"time\": 217, \"liftID\": 21 }";
    LiftRide liftRide = new LiftRide();
    liftRide.setLiftID(liftID);
    liftRide.setTime(time);
    return liftRide;
//    return "{ \"time\" : " + String.valueOf(liftID) + ", \"liftID\" : " + String.valueOf(time) + " }";
  }

  public static void main(String[] args) {
    SkiersGenerator generate = new SkiersGenerator();
//    System.out.println(generate.endpointGenerator());
    System.out.println(generate.jsonGenerator());
  }

}
