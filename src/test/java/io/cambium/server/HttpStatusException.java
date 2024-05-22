package io.cambium.server;

import java.io.IOException;

public class HttpStatusException extends IOException {
  private static final long serialVersionUID = 1L;
  private final int statusCode;

  public HttpStatusException(int statusCode) {
    this.statusCode = statusCode;
  }
  
  public HttpStatusException(int statusCode, String message) {
    super(message);
    this.statusCode = statusCode;
  }
  
  public HttpStatusException(int statusCode, String message, Throwable cause) {
    super(message, cause);
    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return this.statusCode;
  }
  

  
}
