package Part1.Threads;

import Part1.Model.SkiersWrapper;
import io.swagger.client.ApiClient;
import io.swagger.client.api.SkiersApi;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Phase {
  private final Integer numThreads;
  private final Integer numPosts;
  private String phaseOption;
  private SkiersApi skierApi;
  private BlockingQueue<SkiersWrapper> dataBuffer;
  private CountDownLatch phaseSignal;

  public Phase(String phaseOption, Integer numThreads, Integer numPosts, CountDownLatch phaseSignal,
      SkiersApi skierApi, BlockingQueue<SkiersWrapper> dataBuffer, List<Long> latency) {
    this.phaseOption = phaseOption;
    this.numThreads = numThreads;
    this.numPosts = numPosts;
    this.phaseSignal = phaseSignal;
    this.skierApi = skierApi;
    this.dataBuffer = dataBuffer;
  }

  public void startPhase() throws InterruptedException {
    System.out.println(this.phaseOption + " is starting ==========");
    ExecutorService pool = Executors.newFixedThreadPool(this.numThreads);
    for (int i = 0; i < this.numThreads; i++) {
      pool.execute(new Consumer(this.numPosts, this.skierApi, this.dataBuffer, this.phaseSignal, this.latency));
    }
    pool.shutdown();
    while (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
      System.out.println("Awaiting completion of threads.");
    }
  }
}
