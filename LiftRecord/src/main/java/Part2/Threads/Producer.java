package Part2.Threads;

import Part2.Model.SkiersWrapper;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

  private BlockingQueue<SkiersWrapper> dataBuffer;
  private Integer numPosts;

  public Producer(Integer numPosts,
      BlockingQueue<SkiersWrapper> dataBuffer) {
    this.numPosts = numPosts;
    this.dataBuffer = dataBuffer;
  }

  private void generateAndPutSkiersWrapper() throws InterruptedException {
    SkiersWrapper skiersWrapper = new SkiersWrapper();
    dataBuffer.put(skiersWrapper);
  }

  // task for producer thread - generate SkiersWrapper and PostRecords to add ot linked blocking queue
  @Override
  public void run() {
    try {
      for (int i = 0; i < this.numPosts; i++) {
        generateAndPutSkiersWrapper();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
