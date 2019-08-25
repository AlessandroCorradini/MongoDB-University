package mflix.api.daos;

import com.mongodb.ConnectionString;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ConnectionPoolingTest extends TicketTest {

  ConnectionString connectionString;

  @Before
  public void setUp() throws IOException {
    connectionString = new ConnectionString(getProperty("spring.mongodb.uri"));
  }

  @Test
  public void testConnectionPoolSize() {
    Assert.assertNotNull(
        "Do not forget to set the maxPoolSize parameter "
            + "in your spring.mongodb.uri key in the properties file",
        connectionString.getMaxConnectionPoolSize());
    Integer expectedMaxPoolSize = 50;
    Assert.assertEquals(
        "The connection pool size should be set to 50",
        expectedMaxPoolSize,
        connectionString.getMaxConnectionPoolSize());
  }
}
