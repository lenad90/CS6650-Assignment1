package Part1;

import Part1.Model.SkiersWrapper;
import Part1.Threads.Phase;
import Part1.Threads.Producer;
import io.swagger.client.ApiClient;
import io.swagger.client.api.SkiersApi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

public class SkiersClient {
  private static final Integer NUM_POSTS = 200000;

  public static void main(String[] args) throws InterruptedException {
    SkiersApi skierApi = new SkiersApi();
    ApiClient client = skierApi.getApiClient();
    BlockingQueue<SkiersWrapper> dataBuffer = new LinkedBlockingQueue<>();

    client.setBasePath("http://localhost:8080"
      + "/LiftServer_war/");
//    client.setBasePath("http://ec2-34-216-137-133.us-west-2.compute.amazonaws.com:8080/LiftServer_war/");

    new Thread(new Producer(NUM_POSTS, dataBuffer)).start();

    int phase1Threads = 32;
    int phase1Post = 1000;
    int phase2Trigger = phase1Threads/4;

    CountDownLatch phase2Signal = new CountDownLatch(phase2Trigger);
    Phase phase1 = new Phase("Phase 1", phase1Threads, phase1Post, skierApi,
        phase2Signal, dataBuffer);
    phase1.startPhase();

    int phase2Threads = phase1Threads * 2;
    int phase2Post = 1500; //(NUM_POSTS - (phase1Threads*phase1Post))/3;
    CountDownLatch phase3Signal = new CountDownLatch((int) (phase2Threads * 0.1));
    Phase phase2 = new Phase("Phase 2", phase2Threads, phase2Post, skierApi,
        phase3Signal, dataBuffer);
    phase2Signal.await();
    phase2.startPhase();

//
    int phase3Threads = phase2Threads * 2;
    int phase3Post = 1125;
    CountDownLatch phase4Trigger = new CountDownLatch((int) (phase3Threads*0.1));
    Phase phase3 = new Phase("Phase 3", phase2Threads,
        phase3Post, skierApi, phase4Trigger, dataBuffer);
    phase3Signal.await();
    phase3.startPhase();

    phase1.finishPhase();
    phase2.finishPhase();
    phase3.finishPhase();
    phase1.phaseStats();
    phase2.phaseStats();
    phase3.phaseStats();
  }

}
