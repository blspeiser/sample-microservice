package io.cambium.api;

import com.amazonaws.services.lambda.runtime.Context;

import io.cambium.types.models.Location;
import io.cambium.types.requests.SaveLocationRequest;
import io.cambium.types.responses.SaveLocationResponse;
import io.cambium.utils.Strings;

public class SaveLocationFunction extends BaseFunction<SaveLocationRequest, SaveLocationResponse> {
  
  @Override
  public SaveLocationResponse handleRequest(SaveLocationRequest input, Context context) {
    SaveLocationResponse response = new SaveLocationResponse();
    try {
      if(Strings.isBlank(input.locationName)) throw new IllegalArgumentException("No location name provided");
      if(Strings.isBlank(input.timezone))     throw new IllegalArgumentException("No timezone provided");
      Location location = new Location();
      location.id = input.locationName;
      location.timezone = input.timezone;
      repo.upsert(location);
      response.success = true;
      response.errorMessage = "";
    } catch(Exception e) {
      response.success = false;
      response.errorMessage = e.getMessage();
    }
    return response;
  }

}
