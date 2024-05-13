package io.cambium.repositories;

import java.io.IOException;
import java.util.List;

import io.cambium.repositories.sql.SqlLocationRepository;
import io.cambium.types.models.Location;

public interface LocationRepository {
  public static class Factory {
    public static final LocationRepository INSTANCE = new SqlLocationRepository();
  }

  public Location find(String locationName);

  public List<Location> list(String filter);

  public void upsert(Location location) throws IOException;

  public void delete(String location) throws IOException;

}