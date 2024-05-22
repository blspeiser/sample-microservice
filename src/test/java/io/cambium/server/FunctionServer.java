package io.cambium.server;

import java.io.InputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import io.undertow.server.RoutingHandler;

public class FunctionServer {
  private static final int PORT = 8000;
  
  public static void main(String[] args) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      mapper.findAndRegisterModules();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      
      RoutingHandler routes = new RoutingHandler();
      
      routes.get("/api/time", new FunctionHandler(mapper, new FunctionExecutor() {
        public Object execute(InputStream is) throws HttpStatusException, JsonProcessingException {
          GetTimeRequest request = null;
          GetTimeFunction function = new GetTimeFunction();
          try {
            request = mapper.readValue(is, GetTimeRequest.class);
          } catch(Exception e) {
            request = new GetTimeRequest();
          }
          GetTimeResponse response = function.handleRequest(request, null);
          return super.mapper.writeValueAsString(response);  
        }
      }));
      routes.get("/api/locations", new FunctionHandler(mapper, new FunctionExecutor() {
        public Object execute(InputStream is) throws HttpStatusException, JsonProcessingException {
          ListLocationsRequest request = null;
          ListLocationsFunction function = new ListLocationsFunction();
          try {
            request = mapper.readValue(is, ListLocationsRequest.class);
          } catch(Exception e) {
            request = new ListLocationsRequest();
          }
          ListLocationsResponse response = function.handleRequest(request, null);
          return mapper.writeValueAsString(response);  
        }
      }));
      routes.post("/api/locations", new FunctionHandler(mapper, new FunctionExecutor() {
        public Object execute(InputStream is) throws HttpStatusException, JsonProcessingException {
          SaveLocationRequest request = null;
          SaveLocationFunction function = new SaveLocationFunction();
          try {
            request = mapper.readValue(is, SaveLocationRequest.class);
            SaveLocationResponse response = function.handleRequest(request, null);
            return mapper.writeValueAsString(response);
          } catch(Exception e) {
            throw new HttpStatusException(400);
          }  
        }
      }));
      routes.delete("/api/locations", new FunctionHandler(mapper, new FunctionExecutor() {
        public Object execute(InputStream is) throws HttpStatusException, JsonProcessingException {
          DeleteLocationRequest request = null;
          DeleteLocationFunction function = new DeleteLocationFunction();
          try {
            request = mapper.readValue(is, DeleteLocationRequest.class);
            DeleteLocationResponse response = function.handleRequest(request, null);
            return mapper.writeValueAsString(response);
          } catch(Exception e) {
            throw new HttpStatusException(400);
          }
        }
      }));
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
