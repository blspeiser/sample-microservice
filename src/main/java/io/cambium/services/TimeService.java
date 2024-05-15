package io.cambium.services;

import io.cambium.services.impl.TimeServiceBean;
import io.cambium.types.models.Location;
import io.cambium.types.models.TimeInformation;

public interface TimeService {
  public static class Factory {
    public static final TimeService INSTANCE = new TimeServiceBean();
  }

  public TimeInformation getTime();

  public TimeInformation getTime(String timezone);

  public TimeInformation getTime(Location location);

}