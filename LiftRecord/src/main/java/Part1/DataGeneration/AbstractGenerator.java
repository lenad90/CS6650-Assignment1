package Part1.DataGeneration;

import java.security.SecureRandom;
import java.time.LocalDate;

public abstract class AbstractGenerator implements IRandomGenerator {

  protected static final int LOWERBOUND = 1;

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

  public static Integer idGenerated(int upperBound) {
    SecureRandom secureRandom = new SecureRandom();
    return secureRandom.nextInt(upperBound - LOWERBOUND) + LOWERBOUND;
  }



}
