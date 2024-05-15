package io.cambium.api;

import com.amazonaws.services.lambda.runtime.Context;

import io.cambium.types.requests.DeleteLocationRequest;
import io.cambium.types.responses.DeleteLocationResponse;

public class DeleteLocationFunction extends BaseFunction<DeleteLocationRequest, DeleteLocationResponse> {
  
  @Override
  public DeleteLocationResponse handleRequest(DeleteLocationRequest input, Context context) {
    DeleteLocationResponse response = new DeleteLocationResponse();
    try {
      repo.delete(input.location);
      response.success = true;
      response.errorMessage = "";
    } catch(Exception e) {
      response.success = false;
      response.errorMessage = e.getMessage();
    }
    return response;
  }

}
