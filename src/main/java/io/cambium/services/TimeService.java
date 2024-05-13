package io.cambium.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import io.cambium.types.models.Location;
import io.cambium.types.models.TimeInformation;

public class TimeService {
  public static final TimeService INSTANCE = new TimeService();
  
  public TimeInformation getTime() {
    return getTime(ZoneId.systemDefault(), "");
  }
  
  public TimeInformation getTime(String timezone) {
    if(null == timezone) return getTime(ZoneId.systemDefault(), "");
    ZoneId zone = getZone(timezone);
    return getTime(null == zone ? ZoneId.systemDefault() : zone, "");
  }

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
