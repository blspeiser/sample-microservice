package io.cambium.api;

import io.cambium.repositories.LocationRepository;

public class BaseFunction {
  protected LocationRepository repo = LocationRepository.Factory.INSTANCE;

  void setRepository(LocationRepository repo) {
    this.repo = repo;
  }

}