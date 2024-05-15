package io.cambium.services.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import io.cambium.services.TimeService;
import io.cambium.types.models.Location;
import io.cambium.types.models.TimeInformation;

public class TimeServiceBean implements TimeService {
  
  @Override
  public TimeInformation getTime() {
    return getTime(ZoneId.systemDefault(), "");
  }
  
  @Override
  public TimeInformation getTime(String timezone) {
    if(null == timezone) return getTime(ZoneId.systemDefault(), "");
    ZoneId zone = getZone(timezone);
    return getTime(null == zone ? ZoneId.systemDefault() : zone, "");
  }

  @Override
  public TimeInformation getTime(Location location) {
    if(null == location) return getTime(ZoneId.systemDefault(), "");
    ZoneId zone = getZone(location.timezone);
    TimeInformation info = getTime(
        null == zone ? ZoneId.systemDefault() : zone,
        null == zone ? "" : location.id);
    return info;
  }

  private ZoneId getZone(String timezone) {
    return (null == timezone) ? ZoneId.systemDefault() : ZoneId.of(timezone);
  }

  private TimeInformation getTime(ZoneId zone, String location) {
    TimeInformation info = new TimeInformation();
    info.date = LocalDate.now(zone);
    info.time = LocalTime.now(zone);
    info.timestamp = info.date.atTime(info.time).atZone(zone).toInstant().toEpochMilli();
    info.timezone = zone.getId();
    info.location = location;
    return info;
  }

}
