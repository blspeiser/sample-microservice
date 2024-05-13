package io.cambium.api;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.cambium.repositories.LocationRepository;
import io.cambium.types.models.Location;
import io.cambium.types.requests.ListLocationsRequest;
import io.cambium.types.responses.ListLocationsResponse;

public class ListLocationsFunctionTest {
  private boolean useMock = true;

  @Test
  public void test() {
    //Data setup:
    Location location = new Location();
    location.id = "Cambium";
    location.timezone ="Israel";
    location.readOnly = false;
    
    //Function setup:
    ListLocationsFunction function = new ListLocationsFunction();
    LocationRepository repo = useMock
        ? Mockito.mock(LocationRepository.class)
        : LocationRepository.Factory.INSTANCE;
    if(useMock) {
      List<Location> dummyList = Arrays.asList(location);
      Mockito.when(repo.list(null)).thenReturn(dummyList);
      Mockito.when(repo.list("UTC")).thenReturn(dummyList);
    }
    function.setRepository(repo);
    
    //Validation:
    ListLocationsResponse response = function.handleRequest(null, null);
    assertNotNull(response);
    assertNotNull(response.locations);
    assertNotEquals(0, response.locations.length);    
    
    ListLocationsRequest request = new ListLocationsRequest();
    request.filter = "UTC";
    
    response = function.handleRequest(request, null);
    assertNotNull(response);
    assertNotNull(response.locations);
    assertNotEquals(0, response.locations.length);
    
    if(useMock) {
      //Yes, verify that they are actually the exact same instance:
      assertTrue(response.locations[0] == location);
    }
  }
  
}
