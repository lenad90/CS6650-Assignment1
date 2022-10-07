package Part1.Threads;

import Part1.DataGeneration.SkiersGenerator;
import Part1.Model.SkiersWrapper;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

public class LatencyTest {

  public LatencyTest() throws ApiException {

    SkiersApi skierApi = new SkiersApi();
    ApiClient testClient = skierApi.getApiClient();
    SkiersGenerator test = new SkiersGenerator();
    new Thread();
    SkiersWrapper skiersWrapper = new SkiersWrapper();
    ApiResponse<Void> res= skierApi.writeNewLiftRideWithHttpInfo(skiersWrapper.getLiftRide(),
        skiersWrapper.getResortID(), skiersWrapper.getSeasonID(), skiersWrapper.getDay(), skiersWrapper.getSkierId());


  }
}
