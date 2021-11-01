package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PassengerTest {

  private Passenger testPassenger;

  /**
   * Setup operations before each test.
   */
  @BeforeEach
  void setUp() {
    testPassenger = new Passenger(1, "Kobe Bryant");
  }


  /**
   * Test state after using constructor.
   */
  @Test
  void testConstructorNormal() {
    assertEquals("Kobe Bryant", testPassenger.getName());
    assertEquals(1, testPassenger.getDestination());
    assertEquals(0, testPassenger.getWaitAtStop());
    assertEquals(0, testPassenger.getTimeOnVehicle());
  }

  /**
   * Test the state of waitAtStop after using pasUpdate method.
   */
  @Test
  void testPasUpdate() {
    testPassenger.pasUpdate();
    assertEquals(1, testPassenger.getWaitAtStop());
  }

  /**
   * Test the state of a passenger after using setOnVehicle method.
   */
  @Test
  void testSetOnVehicle() {
    testPassenger.setOnVehicle();
    boolean result = testPassenger.isOnVehicle();
    assertTrue(result);
  }

  /**
   * Test the state of a passenger after using isOnVehicle method.
   */
  @Test
  void testIsOnVehicle() {
    assertFalse(testPassenger.isOnVehicle());
    testPassenger.setOnVehicle();
    assertTrue(testPassenger.isOnVehicle());
  }

  /**
   * Test reporting functionality.
   */
  @Test
  void testReport() throws Exception {
    // Setup
    final Charset charset = StandardCharsets.UTF_8;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream testStream = new PrintStream(outputStream, true, charset.name());
    testPassenger.report(testStream);
    outputStream.flush();
    String data = outputStream.toString(charset);
    testStream.close();
    outputStream.close();
    String strToCompare =
        "####Passenger Info Start####" + System.lineSeparator()
            + "Name: Kobe Bryant" + System.lineSeparator()
            + "Destination: 1" + System.lineSeparator()
            + "Wait at stop: 0" + System.lineSeparator()
            + "Time on vehicle: 0" + System.lineSeparator()
            + "####Passenger Info End####" + System.lineSeparator();
    assertEquals(data, strToCompare);
  }
}
