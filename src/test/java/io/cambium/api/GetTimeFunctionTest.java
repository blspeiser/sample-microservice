package io.cambium.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.cambium.repositories.LocationRepository;
import io.cambium.repositories.nosql.MongoLocationRepository;
import io.cambium.repositories.sql.SqlLocationRepository;
import io.cambium.types.models.TimeInformation;
import io.cambium.types.requests.GetTimeRequest;
import io.cambium.types.responses.GetTimeResponse;

public class GetTimeFunctionTest {
  private boolean useSQL = true;
  GetTimeFunction function = new GetTimeFunction();
  
  public GetTimeFunctionTest() {
    LocationRepository repo = useSQL
        ? new SqlLocationRepository()
        : new MongoLocationRepository();
    function.setRepository(repo); 
  }
  
  @Test
  public void testUTC() {
    GetTimeRequest request = new GetTimeRequest();
    request.location = "UTC";
    GetTimeResponse response = function.handleRequest(request, null);
    assertNotNull(response);
    TimeInformation info = response.time;
    assertNotNull(info);
    assertNotNull(info.date);
    assertNotNull(info.time);
    assertNotNull(info.timestamp);
    assertNotNull(info.timezone);
    assertNotNull(info.location);
    assertEquals("UTC", info.timezone);
    assertEquals("UTC", info.location);
  }

  @Test
  public void testCambium() {
    GetTimeRequest request = new GetTimeRequest();
    request.location = "Cambium";
    GetTimeResponse response = function.handleRequest(request, null);
    assertNotNull(response);
    TimeInformation info = response.time;
    assertNotNull(info);
    assertNotNull(info.date);
    assertNotNull(info.time);
    assertNotNull(info.timestamp);
    assertNotNull(info.timezone);
    assertNotNull(info.location);
    assertEquals("Israel", info.timezone);
    assertEquals("Cambium", info.location);
    System.out.println(info.date + " " + info.time.toString().substring(0,8));
    
  }

}