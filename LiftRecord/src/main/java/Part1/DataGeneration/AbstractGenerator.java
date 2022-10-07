package Part1.DataGeneration;

import java.security.SecureRandom;
import java.time.LocalDate;

public abstract class AbstractGenerator implements IRandomGenerator {

  protected static final int LOWERBOUND = 1;

  // skierID - 1 - 100000
  // resortID - 1 - 10
  // liftID -  1- 140
  // seasonID - LocalDateTime getCurrentYear
  // dayID - 1
  // time - 1 - 360

 public Integer generateResortID() {
   return AbstractGenerator.idGenerated(10);
 }

 public String generateSeasonID() {
   return String.valueOf(LocalDate.now().getYear());
 }

 public String getDay() {
   return String.valueOf(LOWERBOUND);
 }

 public Integer generateSkierID() {
   return AbstractGenerator.idGenerated(100000);
 }

//  @Override
//  public String endpointGenerator() {
//    int skierID = AbstractGenerator.idGenerated(100000);
//    int resortID = AbstractGenerator.idGenerated(10);
//
//    // /123/seasons/123/days/123/skiers/123
//    return "/" + String.valueOf(resortID) + "/seasons/" + String.valueOf(LocalDate.now().getYear())
//        + "/days/" + String.valueOf(DAY) + "/skiers/" + String.valueOf(skierID);
//  }

  public static Integer idGenerated(int upperBound) {
    SecureRandom secureRandom = new SecureRandom();
    return secureRandom.nextInt(upperBound - LOWERBOUND) + LOWERBOUND;
  }



}
