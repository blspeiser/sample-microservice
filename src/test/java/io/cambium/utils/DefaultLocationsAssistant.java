package io.cambium.utils;

import java.time.ZoneId;

@SuppressWarnings("unused")
public class DefaultLocationsAssistant {
  
  public static void main(String[] args) {
    try {
      printJSON();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  private static void printSQL() {
    ZoneId.getAvailableZoneIds().forEach(s -> System.out.println(
        String.format("INSERT INTO TIME_SERVICE.TS_LOCATIONS (ID, NAME, TIMEZONE, TYPE) "
                    + "VALUES (SELECT NEXTVAL('SEQ_TS_LOCATIONS'), '%s', '%s', 1);", s, s)
        ));
  }
  
  private static void printJSON() {
    ZoneId.getAvailableZoneIds().forEach(s -> System.out.println(
        String.format("{ \"_id\" : \"%s\", \"timezone\": \"%s\", \"readOnly\": true, \"userCreated\": false },", s, s)
        ));
  }

}
