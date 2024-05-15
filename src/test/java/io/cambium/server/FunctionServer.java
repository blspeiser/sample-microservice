package io.cambium.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.cambium.api.DeleteLocationFunction;
import io.cambium.api.GetTimeFunction;
import io.cambium.api.ListLocationsFunction;
import io.cambium.api.SaveLocationFunction;
import io.cambium.types.requests.DeleteLocationRequest;
import io.cambium.types.requests.GetTimeRequest;
import io.cambium.types.requests.ListLocationsRequest;
import io.cambium.types.requests.SaveLocationRequest;
import io.cambium.types.responses.DeleteLocationResponse;
import io.cambium.types.responses.GetTimeResponse;
import io.cambium.types.responses.ListLocationsResponse;
import io.cambium.types.responses.SaveLocationResponse;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;

public class FunctionServer {
  private static final int PORT = 8000;
  
  public static void main(String[] args) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.findAndRegisterModules();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      
      RoutingHandler routes = new RoutingHandler();
      
      routes.get("/api/time", new HttpHandler() {
        public void handleRequest(HttpServerExchange exchange) throws Exception {
          if(exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
          }
          try {
            exchange.startBlocking();
            GetTimeRequest request = null;
            GetTimeFunction function = new GetTimeFunction();
            try {
              request = mapper.readValue(exchange.getInputStream(), GetTimeRequest.class);
            } catch(Exception e) {
              request = new GetTimeRequest();
            }
            GetTimeResponse response = function.handleRequest(request, null);
            exchange.getResponseSender().send(mapper.writeValueAsString(response));
          } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
          }  
        }
      });
      routes.get("/api/locations", new HttpHandler() {
        public void handleRequest(HttpServerExchange exchange) throws Exception {
          if(exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
          }
          try {
            exchange.startBlocking();
            ListLocationsRequest request = null;
            ListLocationsFunction function = new ListLocationsFunction();
            try {
              request = mapper.readValue(exchange.getInputStream(), ListLocationsRequest.class);
            } catch(Exception e) {
              request = new ListLocationsRequest();
            }
            ListLocationsResponse response = function.handleRequest(request, null);
            exchange.getResponseSender().send(mapper.writeValueAsString(response));
          } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
          }  
        }
      });
      routes.post("/api/locations", new HttpHandler() {
        public void handleRequest(HttpServerExchange exchange) throws Exception {
          if(exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
          }
          try {
            exchange.startBlocking();
            SaveLocationRequest request = null;
            SaveLocationFunction function = new SaveLocationFunction();
            try {
              request = mapper.readValue(exchange.getInputStream(), SaveLocationRequest.class);
              SaveLocationResponse response = function.handleRequest(request, null);
              exchange.getResponseSender().send(mapper.writeValueAsString(response));
            } catch(Exception e) {
              exchange.setStatusCode(400);
            }
          } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
          }  
        }
      });
      routes.delete("/api/locations", new HttpHandler() {
        public void handleRequest(HttpServerExchange exchange) throws Exception {
          if(exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
          }
          try {
            exchange.startBlocking();
            DeleteLocationRequest request = null;
            DeleteLocationFunction function = new DeleteLocationFunction();
            try {
              request = mapper.readValue(exchange.getInputStream(), DeleteLocationRequest.class);
              DeleteLocationResponse response = function.handleRequest(request, null);
              exchange.getResponseSender().send(mapper.writeValueAsString(response));
            } catch(Exception e) {
              exchange.setStatusCode(400);
            }
          } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
          }  
        }
      });
      routes.setFallbackHandler(exchange -> {
        exchange.setStatusCode(404);
      });
      
      Undertow server = Undertow.builder()
          .addHttpListener(PORT, "127.0.0.1")
          .setHandler(routes)
          .build();
      server.start();
      
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  
  

}
