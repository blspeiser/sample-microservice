package io.cambium.repositories.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import io.cambium.repositories.LocationRepository;
import io.cambium.types.models.Location;
import io.cambium.utils.DatabaseUtils;
import io.cambium.utils.Strings;

public class SqlLocationRepository implements LocationRepository {
  DataSource ds = DataSourceFactory.createDataSource();

  @Override
  public Location find(String locationName) {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = ds.getConnection();
      stmt = conn.prepareStatement("SELECT TIMEZONE, TYPE FROM TIMES_SERVICE.TS_LOCATIONS WHERE NAME = ?");
      stmt.setString(1, locationName);
      rs = stmt.executeQuery();
      Location location = null;
      while(rs.next()) {
        location = new Location();
        location.id       = locationName;
        location.timezone = rs.getString(1);
        location.readOnly = (rs.getInt(2) == 1);
      }
      return location;
    } catch(SQLException e) {
      throw new RuntimeException("Unexpected error", e);
    } finally {
      DatabaseUtils.close(conn, stmt, rs);
    }
  }

  @Override
  public List<Location> list(String filter) {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      String sql = Strings.isBlank(filter)
          ? "SELECT NAME, TIMEZONE, TYPE FROM TIMES_SERVICE.TS_LOCATIONS"
          : "SELECT NAME, TIMEZONE, TYPE FROM TIMES_SERVICE.TS_LOCATIONS WHERE NAME LIKE ?";
      conn = ds.getConnection();
      stmt = conn.prepareStatement(sql);
      if(!Strings.isBlank(filter)) {
        stmt.setString(1, "%" + filter + "%");
      }
      rs = stmt.executeQuery();
      List<Location> list = new ArrayList<Location>();
      while(rs.next()) {
        Location location = new Location();
        location.id       = rs.getString(1);
        location.timezone = rs.getString(2);
        location.readOnly = (rs.getInt(3) == 1);
        list.add(location);
      }
      return list;
    } catch(SQLException e) {
      throw new RuntimeException("Unexpected error", e);
    } finally {
      DatabaseUtils.close(conn, stmt, rs);
    }
  }

  @Override
  public void upsert(Location location) throws IOException {
    if(!ZoneId.getAvailableZoneIds().contains(location.timezone)) {
      throw new IllegalArgumentException("Not a valid timezone");
    }
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = ds.getConnection();
      stmt = conn.prepareStatement("SELECT TYPE FROM TIMES_SERVICE.TS_LOCATIONS WHERE NAME = ?");
      stmt.setString(1, location.id);
      rs = stmt.executeQuery();
      if(rs.next()) {
        int type = rs.getInt(1);
        if(type == 1) throw new IOException("Cannot update a read-only location");
        DatabaseUtils.close(stmt, rs);
        rs = null;
        stmt = conn.prepareStatement("UPDATE TIMES_SERVICE.TS_LOCATIONS SET TIMEZONE = ? WHERE NAME = ?");
        stmt.setString(1, location.timezone);
        stmt.setString(2, location.id);
        stmt.executeUpdate();
      } else {
        DatabaseUtils.close(stmt, rs);
        rs = null;
        stmt = conn.prepareStatement(
          "INSERT INTO TIMES_SERVICE.TS_LOCATIONS " +
          "(ID, NAME, TIMEZONE, TYPE) VALUES " + 
          "(NEXTVAL('TIMES_SERVICE.SEQ_TS_LOCATIONS'), ?, ?, 3)");
        stmt.setString(1, location.id);
        stmt.setString(2, location.timezone);
        stmt.executeUpdate();
      }
    } catch(SQLException e) {
      throw new RuntimeException("Unexpected error", e);
    } finally {
      DatabaseUtils.close(conn, stmt, rs);
    }
    
  }

  @Override
  public void delete(String location) throws IOException {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = ds.getConnection();
      stmt = conn.prepareStatement("DELETE FROM TIMES_SERVICE.TS_LOCATIONS WHERE TYPE > 1 AND NAME = ?");
      stmt.setString(1, location);
      int deleted = stmt.executeUpdate();
      if(deleted == 0) throw new IOException("Could not delete location - either it does not exist, or it is read-only");
    } catch(SQLException e) {
      throw new RuntimeException("Unexpected error", e);
    } finally {
      DatabaseUtils.close(conn, stmt);
    }
  }

}
