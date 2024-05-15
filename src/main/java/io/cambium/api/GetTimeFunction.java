package io.cambium.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import io.cambium.services.TimeService;
import io.cambium.types.models.Location;
import io.cambium.types.requests.GetTimeRequest;
import io.cambium.types.responses.GetTimeResponse;
import io.cambium.utils.Strings;

public class GetTimeFunction extends BaseFunction implements RequestHandler<GetTimeRequest, GetTimeResponse> {
  private TimeService service = TimeService.Factory.INSTANCE;

  @Override
  public GetTimeResponse handleRequest(GetTimeRequest input, Context context) {
    GetTimeResponse response = new GetTimeResponse();
    //Favor explicit timezone over location
    if(null != input && !Strings.isBlank(input.timezone)) { 
      response.time = service.getTime(input.timezone);
    } else
    if(null != input && !Strings.isBlank(input.location)) {
      Location location = repo.find(input.location);
      response.time = service.getTime(location);
    } else {
      response.time = service.getTime();
    } 
    return response;
  }

}
