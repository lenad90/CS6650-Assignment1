package Part1.DataGeneration;

import io.swagger.client.model.LiftRide;

// TODO: create a wrapper
public class SkiersGenerator extends AbstractGenerator implements IRandomGenerator {

  public SkiersGenerator() {
  }


  @Override
  public LiftRide jsonGenerator() {
    Integer liftID = AbstractGenerator.idGenerated(40);
    Integer time = AbstractGenerator.idGenerated(360);
//    "{ \"time\": 217, \"liftID\": 21 }";
    LiftRide liftRide = new LiftRide();
    liftRide.setLiftID(liftID);
    liftRide.setTime(time);
    return liftRide;
  }
}
