package io.cambium.repositories.nosql;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnectionFactory {
  private static final String DATABASE_NAME = "app";
  private static final String PROPERTY_PREFIX    = "io.cambium.database";
 
  public static final String PROPERTY_URL       = PROPERTY_PREFIX + ".url";
  
  private static final String URL = System.getProperty(PROPERTY_URL,      "mongodb://localhost:27017/app");
  
  private static MongoClient client;
  private static MongoDatabase database;
  
  static {
    MongoConnectionFactory.client = MongoClients.create(URL);
    MongoConnectionFactory.database = client.getDatabase(DATABASE_NAME); 
  }
  
  public static final MongoDatabase connect() {
    return database;
  }

  
}
