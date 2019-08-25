package mflix.api.daos;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Random;

public class TicketTest {
  private static Properties properties;

  protected static String getProperty(String propertyKey) throws IOException {

    if (properties == null) {
      TicketTest.init();
    }

    return properties.getProperty(propertyKey);
  }

  protected static void init() throws IOException {
    properties = new Properties();
    properties.load(ClassLoader.getSystemResourceAsStream("application.properties"));
  }

  protected String randomText() {
    return randomText(10);
  }

  protected String randomText(int size) {
    byte[] array = new byte[size];
    new Random().nextBytes(array);
    return new String(array, Charset.forName("UTF-8"));
  }
}
