package Part2;

import Part2.Model.SkiersWrapper;
import Part2.Threads.Phase;
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
  private static final Integer NUM_POSTS = 200000;

  public static void main(String[] args) throws InterruptedException {
    SkiersApi skierApi = new SkiersApi();
    ApiClient client = skierApi.getApiClient();
    BlockingQueue<SkiersWrapper> dataBuffer = new LinkedBlockingQueue<>();

    client.setBasePath("http://localhost:8080"
        + "/LiftServer_war/");
//    client.setBasePath("http://34.221.94.117:8080"
//        + "/LiftServer_war/");

    File file = new File("LiftRecordPerformance.csv");
    dataPerformance.add(new String[]{"Start Time", "Request Type", "Latency", "Response Code"});

    new Thread(new Producer(NUM_POSTS, dataBuffer)).start();

//    int numPostPhase = 1000;
//    int numThreads = 32;

    int phase1Threads = 32;
    int phase1Post = 1000;
    int phase2Trigger = phase1Threads/4;
    CountDownLatch phase2Signal = new CountDownLatch(phase2Trigger);
    Phase phase1 = new Phase("Phase 1", phase1Threads, phase1Post, phase2Signal, skierApi,
        dataBuffer);
    phase1.startPhase2();
    //56,000
    int phase2Threads = phase1Threads * 2;
    int phase2PostTotal = (NUM_POSTS - phase1Post * phase1Threads)/3;
    int phase3Trigger = (int) Math.round(phase2Threads * 0.1);
    CountDownLatch phase3Signal = new CountDownLatch(phase3Trigger);
    Phase phase2 = new Phase("Phase 2", phase2Threads,
        phase2PostTotal/phase2Threads, phase3Signal, skierApi, dataBuffer);
    phase2.startPhase2();
    phase2.await2();
//
    int phase3Threads = phase2Threads * 2;
    int phase3PostTotal = (phase3Threads*phase2Threads*10);
    CountDownLatch phase4Trigger = new CountDownLatch(2);
    Phase phase3 = new Phase("Phase 3", phase2Threads,
        phase3PostTotal/phase3Threads, phase4Trigger, skierApi, dataBuffer);
    phase3.startPhase2();
    phase3.await2();

    int phase4Threads = phase3Threads * 2;
    int phase4PostTotal = (NUM_POSTS - (phase3PostTotal + phase2PostTotal));
    CountDownLatch completion = new CountDownLatch(phase3Threads);
    Phase phase4 = new Phase("Phase 4", phase2Threads,
        phase4PostTotal/phase4Threads, completion, skierApi, dataBuffer);
    phase4.startPhase2();
    phase4.await2();


    try {
      BufferedWriter outputFile = new BufferedWriter(new FileWriter(file));
      CSVWriter writer = new CSVWriter(outputFile);
      writer.writeAll(dataPerformance);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    phase1.phaseStats2();
    phase2.phaseStats2();
    phase3.phaseStats2();
    phase4.phaseStats2();
  }

}
