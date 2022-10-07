package Part1.Threads;

import Part1.SkiersClient;
import Part1.Model.SkiersWrapper;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Consumer implements Runnable {

  private SkiersApi skierApi;
  private BlockingQueue<SkiersWrapper> dataBuffer;
  private Integer numPosts;
  private CountDownLatch phaseSignal;
  private List<Long> latency;

  public Consumer() {
  }

  public Consumer(Integer numPosts, SkiersApi skierApi,
      BlockingQueue<SkiersWrapper> dataBuffer, CountDownLatch phaseSignal, List<Long> latency) {
    this.skierApi = skierApi;
    this.dataBuffer = dataBuffer;
    this.numPosts = numPosts;
    this.phaseSignal = phaseSignal;
    this.latency = latency;
  }

  @Override
  public void run() {
    SkiersWrapper skier;
    for (int i = 0; i < this.numPosts; i++) {
      try {
        skier = this.dataBuffer.take();
        this.post(skier);
      } catch (InterruptedException | ApiException e) {
        e.printStackTrace();
        System.out.println(e);
      }
    }
  }

  synchronized private void post(SkiersWrapper skier) throws ApiException {
    int numTries = 0;
    try {
      while (numTries != 5) {
        long startPost = System.currentTimeMillis();
        ApiResponse<Void> response = skierApi.writeNewLiftRideWithHttpInfo(skier.getLiftRide(),
            skier.getResortID(), skier.getSeasonID(), skier.getDay(), skier.getSkierId());
        long endPost = System.currentTimeMillis();
        SkiersClient.latency.add((endPost - startPost));
        if (response.getStatusCode() == 201 | response.getStatusCode() == 200) {
          SkiersClient.SUCCESSFUL.getAndIncrement();
          this.phaseSignal.countDown();
          break;
        } else {
          numTries += 1;
        }
      }
      SkiersClient.UNSUCCESSFUL.getAndIncrement();
      if (numTries == 5) {
        throw new ApiException("Network is down");
      }
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }
}
