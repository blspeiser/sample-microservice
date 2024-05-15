package io.cambium.api;

import com.amazonaws.services.lambda.runtime.RequestHandler;

import io.cambium.repositories.LocationRepository;

public abstract class BaseFunction<REQUEST, RESPONSE> implements RequestHandler<REQUEST, RESPONSE> {
  protected LocationRepository repo = LocationRepository.Factory.INSTANCE;

  void setRepository(LocationRepository repo) {
    this.repo = repo;
  }

}