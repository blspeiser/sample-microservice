package io.cambium.api;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import io.cambium.types.models.Location;
import io.cambium.types.requests.ListLocationsRequest;
import io.cambium.types.responses.ListLocationsResponse;

public class ListLocationsFunction extends BaseFunction implements RequestHandler<ListLocationsRequest, ListLocationsResponse> {
  
  @Override
  public ListLocationsResponse handleRequest(ListLocationsRequest input, Context context) {
    ListLocationsResponse response = new ListLocationsResponse();
    List<Location> list = repo.list(null == input ? null : input.filter);
    response.locations = new Location[list.size()];
    list.toArray(response.locations); 
    return response;
  }

}
