package io.cambium.repositories.sql;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * DataSourceFactory.
 * 
 *   Creates and manages a DataSource instance via properties configuration. 
 *
 * @author Baruch Speiser, Cambium.
 *
 */
public class DataSourceFactory {
  private static final String PROPERTY_PREFIX    = "io.cambium.database";
  
  public static final String PROPERTY_DRIVER    = PROPERTY_PREFIX + ".driver";
  public static final String PROPERTY_URL       = PROPERTY_PREFIX + ".url";
  public static final String PROPERTY_USERNAME  = PROPERTY_PREFIX + ".username";
  public static final String PROPERTY_PASSWORD  = PROPERTY_PREFIX + ".password";
  
  private static final String DRIVER    = System.getProperty(PROPERTY_DRIVER,   "org.postgresql.Driver");
  private static final String URL       = System.getProperty(PROPERTY_URL,      "jdbc:postgresql://localhost:5432/app");
  private static final String USERNAME  = System.getProperty(PROPERTY_USERNAME, "postgres");
  private static final String PASSWORD  = System.getProperty(PROPERTY_PASSWORD, "postgres");
  
  private static DataSource dataSource;
  
  static {
    BasicDataSource dataSource = new BasicDataSource();
    dataSource.setDriverClassName(DRIVER);
    dataSource.setUrl(URL);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    DataSourceFactory.dataSource = dataSource;   
  }
  
  /** @return a configured data source instance  */
  public static final DataSource createDataSource() {
    return dataSource;
  }

  
}
