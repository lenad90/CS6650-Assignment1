package Part2;

import Part2.Threads.Phase;
import Part2.Model.SkiersRunner;
import Part2.Threads.Producer;
import com.opencsv.CSVWriter;
import io.swagger.client.ApiClient;
import io.swagger.client.api.SkiersApi;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class SkiersClient2 {
  public static final List<String[]> dataPerformance = Collections.synchronizedList(new ArrayList<>());
  public static final List<Long> responseTime = Collections.synchronizedList(new ArrayList<>());
  private static final Integer NUM_POSTS = 200000;

  public static void main(String[] args) throws InterruptedException {
    int processors = Runtime.getRuntime().availableProcessors();

    SkiersApi skierApi = new SkiersApi();
    ApiClient client = skierApi.getApiClient();
    BlockingQueue<SkiersRunner> dataBuffer2 = new LinkedBlockingQueue<>();

    client.setBasePath("http://ec2-35-93-20-47.us-west-2.compute.amazonaws.com:8080/LiftSpringServerWar");

    File file = new File("LiftRecordPerformanceSpring.csv");
    dataPerformance.add(new String[]{"Start Time", "Request Type", "Latency", "Response Code"});

    new Thread(new Producer(NUM_POSTS, dataBuffer2)).start();

    int phase1Threads = processors*4;
    int phase1Post = 2050;
    int phase2Trigger = phase1Threads/4;

    long start = System.currentTimeMillis();
    CountDownLatch phase2Signal = new CountDownLatch(phase2Trigger);
    Phase phase1 = new Phase("Phase 1", phase1Threads, phase1Post, skierApi,
        phase2Signal, dataBuffer2);
    phase1.startPhase();

    int phase2Threads = phase1Threads * 2;
    int phase2Post = 1500;
    CountDownLatch phase3Signal = new CountDownLatch((int) (phase2Threads * 0.1));
    Phase phase2 = new Phase("Phase 2", phase2Threads, phase2Post, skierApi,
        phase3Signal, dataBuffer2);
    phase2Signal.await();
    phase2.startPhase();

    int phase3Threads = phase2Threads * 2;
    int phase3Post = 300;
    CountDownLatch phase4Trigger = new CountDownLatch((int) (phase3Threads*0.1));
    Phase phase3 = new Phase("Phase 3", phase3Threads,
        phase3Post, skierApi, phase4Trigger, dataBuffer2);
    phase3Signal.await();
    phase3.startPhase();

    phase1.finishPhase();
    phase2.finishPhase();
    phase3.finishPhase();
    long end = System.currentTimeMillis();


    try {
      BufferedWriter outputFile = new BufferedWriter(new FileWriter(file));
      CSVWriter writer = new CSVWriter(outputFile);
      SkiersClient2.dataPerformance.add(new String[] { "Start Time", "Request Type",
          "Latency", "Response Code" });
      writer.writeAll(dataPerformance);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Calculations calc = new Calculations(responseTime);
    System.out.println("============= PART 2 STATS ==============");
    System.out.println("Mean response time = " + calc.mean() + "/ms");
    System.out.println("Median response time = " + calc.median() + "/ms");
    System.out.println("Throughput = " +
        NUM_POSTS/(int) (end - start) + "/ms");
    System.out.println("p99 Response Time = " + calc.percentile(99) + "/ms");
    System.out.println("Min = " + calc.min() + "/s" + " Max = " + calc.max() + "/ms");


  }

}
