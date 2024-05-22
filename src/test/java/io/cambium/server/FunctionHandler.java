package io.cambium.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class FunctionHandler implements HttpHandler {
  private final ObjectMapper mapper;
  private final FunctionExecutor executor;

  public FunctionHandler(ObjectMapper mapper, FunctionExecutor executor) {
    this.mapper = mapper;
    this.executor = executor;
    this.executor.setMapper(mapper);
  }

  @Override
  public void handleRequest(HttpServerExchange exchange) throws Exception {
    if(exchange.isInIoThread()) {
      exchange.dispatch(this);
      return;
    }
    try {
      exchange.startBlocking();
      Object response = this.executor.execute(exchange.getInputStream());
      exchange.getResponseSender().send(this.mapper.writeValueAsString(response));
    } catch(HttpStatusException e) {
      exchange.setStatusCode(e.getStatusCode());
    } catch(Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }  
  }

}
