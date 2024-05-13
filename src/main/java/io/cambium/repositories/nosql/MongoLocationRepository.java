package io.cambium.repositories.nosql;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import io.cambium.repositories.LocationRepository;
import io.cambium.types.models.Location;
import io.cambium.utils.Strings;

public class MongoLocationRepository implements LocationRepository {
  private static final String LOCATIONS     = "Locations";
  private static final String ID            = "_id";
  private static final String TIMEZONE      = "timezone";
  private static final String READ_ONLY     = "readOnly";
  private static final String USER_CREATED  = "userCreated";
  
  @Override
  public Location find(String locationName) {
    Bson filter = Filters.eq(ID, locationName);
    FindIterable<Document> results = getCollection().find(filter);
    Document d = results.first();
    return (null == d) ? null : toLocation(d);
  }

  @Override
  public List<Location> list(String filter) {
    List<Location> list = new ArrayList<>();
    FindIterable<Document> results = Strings.isBlank(filter)
        ? getCollection().find()
        : getCollection().find(Filters.regex(ID, Pattern.quote(filter)));
    MongoCursor<Document> itr = results.iterator();
    while(itr.hasNext()) {
      Document d = itr.next();
      Location location = toLocation(d);
      list.add(location);
    }
    return list;
  }

  @Override
  public void upsert(Location location) throws IOException {
    Bson filter = Filters.eq(ID, location.id);
    List<Bson> updates = Arrays.asList(
        Updates.set(TIMEZONE,     location.timezone),
        Updates.set(READ_ONLY,    false),
        Updates.set(USER_CREATED, true)
    );
    UpdateOptions upsert = new UpdateOptions().upsert(true);
    getCollection().updateOne(filter, updates, upsert);
  }

  @Override
  public void delete(String locationName) throws IOException {
    Bson filter = Filters.and(
        Filters.eq(ID,        locationName),
        Filters.ne(READ_ONLY, true));
    getCollection().findOneAndDelete(filter);
  }

  private MongoCollection<Document> getCollection() {
    return MongoConnectionFactory.connect().getCollection(LOCATIONS);
  }

  private Location toLocation(Document d) {
    Location location = new Location();
    location.id       = d.getString(ID);
    location.timezone = d.getString(TIMEZONE);
    location.readOnly = d.getBoolean(READ_ONLY, false);
    return location;
  }

}
