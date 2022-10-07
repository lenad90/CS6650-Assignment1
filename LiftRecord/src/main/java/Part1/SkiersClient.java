package Part1;

import static java.lang.Math.round;
import Part1.Model.SkiersWrapper;
import Part1.Threads.Phase;
import Part1.Threads.Producer;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import Part1.DataGeneration.SkiersGenerator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class SkiersClient {
  public static final AtomicInteger SUCCESSFUL = new AtomicInteger(0);
  public static final AtomicInteger UNSUCCESSFUL = new AtomicInteger(0);
  private static final Integer NUM_PRODUCER_THREADS = 1;
  private static final Integer NUM_CONSUMER_THREADS = 200;
  private static final Integer NUM_POSTS = 200000;
  public static List<Long> latency = Collections.synchronizedList(new ArrayList<>());

  public static void main(String[] args) throws ApiException, InterruptedException {
    SkiersApi skierApi = new SkiersApi();
    ApiClient client = skierApi.getApiClient();
    BlockingQueue<SkiersWrapper> dataBuffer = new LinkedBlockingQueue<>();
    List<Long> latency = Collections.synchronizedList(new ArrayList<>());

    client.setBasePath("http://localhost:8080"
        + "/Lab2_war_exploded/");


    new Thread(new Producer(NUM_PRODUCER_THREADS, NUM_POSTS, dataBuffer)).start();

    int numPostPhase = NUM_POSTS/NUM_CONSUMER_THREADS;
    int numThreads = NUM_CONSUMER_THREADS/4;

//    int numPostPhase = 1000;
//    int numThreads = 32;

    long start = System.currentTimeMillis();

    CountDownLatch phase2Signal = new CountDownLatch(numThreads);
    Phase phase1 = new Phase("Phase1", numThreads, numPostPhase, phase2Signal, skierApi,
        dataBuffer, latency);
    phase1.startPhase();
    phase2Signal.await();


    CountDownLatch phase3Signal = new CountDownLatch(numThreads);
    Phase phase2 = new Phase("Phase2", numThreads, numPostPhase, phase3Signal, skierApi,
        dataBuffer, latency);
    phase2.startPhase();
    phase3Signal.await();

    CountDownLatch phase4Signal = new CountDownLatch(numThreads);
    Phase phase3 = new Phase("Phase3", numThreads, numPostPhase, phase4Signal, skierApi,
        dataBuffer, latency);
    phase3.startPhase();
    phase4Signal.await();

    CountDownLatch completeSignal = new CountDownLatch(numThreads);
    Phase phase4 = new Phase("Phase4", numThreads, numPostPhase, completeSignal, skierApi,
        dataBuffer, latency);
    phase4.startPhase();
    completeSignal.await();


    long end = System.currentTimeMillis();

    long runtime = end - start;;
    System.out.println("Number of successful POST requests: " + SUCCESSFUL);
    System.out.println("Number of unsuccessful POST requests: " + UNSUCCESSFUL);
    System.out.println("Wall time in seconds: " + (runtime*0.001));
    System.out.println("Actual Throughput = " + round(UNSUCCESSFUL.intValue()
        + SUCCESSFUL.intValue() / (runtime*0.001)));
    System.out.println(latency.size());
  }

}
