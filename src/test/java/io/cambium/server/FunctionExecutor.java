package io.cambium.server;

import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class FunctionExecutor {
  protected ObjectMapper mapper;
  
  void setMapper(ObjectMapper mapper) {
    this.mapper = mapper;
  }
  
  public abstract Object execute(InputStream is) 
      throws HttpStatusException, JsonProcessingException;

}
