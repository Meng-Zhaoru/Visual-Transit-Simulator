package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RouteTest {

  private Route testRoute;

  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  void setUp() {
    testRoute = createRoute();
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Create a route with 3 stops.
   */
  public Route createRoute() {
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    Stop stop3 = new Stop(2, "test stop 3", new Position(-93.226632, 44.975392));
    List<Stop> stopsOut = new ArrayList<>();
    stopsOut.add(stop1);
    stopsOut.add(stop2);
    stopsOut.add(stop3);
    List<Double> distancesOut = new ArrayList<>();
    distancesOut.add(0.9712663713083954);
    distancesOut.add(0.961379387775189);
    List<Double> probabilitiesOut = new ArrayList<>();
    probabilitiesOut.add(0.2);
    probabilitiesOut.add(0.2);
    probabilitiesOut.add(0.2);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(stopsOut, probabilitiesOut);
    return new Route(10, "testLine", "BUS", "testRouteOut",
        stopsOut, distancesOut, generatorOut);
  }

  /**
   * Testing state after using constructor.
   */
  @Test
  public void testConstructorNormal() {
    assertEquals(10, testRoute.getId());
    assertEquals("testLine", testRoute.getLineName());
    assertEquals("BUS", testRoute.getLineType());
    assertEquals("testRouteOut", testRoute.getName());
    assertEquals(3, testRoute.getStops().size());
    assertEquals("test stop 1", testRoute.getStops().get(0).getName());
    assertEquals("test stop 2", testRoute.getStops().get(1).getName());
    assertEquals("test stop 3", testRoute.getStops().get(2).getName());
    assertTrue(testRoute.getNextStop().equals(testRoute.getStops().get(0)));
    assertEquals(0, testRoute.getNextStopIndex());
    assertEquals(2, testRoute.getDistances().size());
    assertEquals(0.0, testRoute.getNextStopDistance());
    testRoute.nextStop();
    assertEquals(0.9712663713083954, testRoute.getNextStopDistance());
    testRoute.nextStop();
    assertEquals(0.961379387775189, testRoute.getNextStopDistance());
    assertEquals(2, testRoute.generateNewPassengers());
  }

  /**
   * Testing shallow copy functionality.
   */
  @Test
  void testShallowCopy() {
    Route testRoute2 = testRoute.shallowCopy();
    assertEquals(testRoute.getId(), testRoute2.getId());
    assertEquals(testRoute.getLineName(), testRoute2.getLineName());
    assertEquals(testRoute.getLineType(), testRoute2.getLineType());
    assertEquals(testRoute.getStops(), testRoute2.getStops());
    assertEquals(testRoute.getNextStopDistance(), testRoute2.getNextStopDistance());
    testRoute.nextStop();
    testRoute2.nextStop();
    assertEquals(testRoute.getNextStopDistance(), testRoute2.getNextStopDistance());
    assertEquals(testRoute.generateNewPassengers(), testRoute2.generateNewPassengers());
  }

  /**
   * Testing the stops after using update method.
   */
  @Test
  void testUpdate() {
    testRoute.update();
    assertEquals(2, PassengerFactory.DETERMINISTIC_NAMES_COUNT);
    assertEquals(1, testRoute.getStops().get(0).getPassengers().size());
    assertEquals(1, testRoute.getStops().get(1).getPassengers().size());
    assertEquals(0, testRoute.getStops().get(2).getPassengers().size());
  }

  /**
   * Testing reporting functionality.
   */
  @Test
  void testReport() throws IOException {
    final Charset charset = StandardCharsets.UTF_8;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream testStream = new PrintStream(outputStream, true, charset.name());
    testRoute.report(testStream);
    outputStream.flush();
    String data = outputStream.toString(charset);
    System.out.println(data);
    testStream.close();
    outputStream.close();
    String strToCompare =
        "####Route Info Start####" + System.lineSeparator()
            + "ID: 10" + System.lineSeparator()
            + "Line name: testLine" + System.lineSeparator()
            + "Line type: BUS" + System.lineSeparator()
            + "Name: testRouteOut" + System.lineSeparator()
            + "Num stops: 3" + System.lineSeparator()
            + "****Stops Info Start****" + System.lineSeparator()
            + "++++Next Stop Info Start++++" + System.lineSeparator()
            + "####Stop Info Start####" + System.lineSeparator()
            + "ID: 0" + System.lineSeparator()
            + "Name: test stop 1" + System.lineSeparator()
            + "Position: 44.972392,-93.243774" + System.lineSeparator()
            + "****Passengers Info Start****" + System.lineSeparator()
            + "Num passengers waiting: 0" + System.lineSeparator()
            + "****Passengers Info End****" + System.lineSeparator()
            + "####Stop Info End####" + System.lineSeparator()
            + "++++Next Stop Info End++++" + System.lineSeparator()
            + "####Stop Info Start####" + System.lineSeparator()
            + "ID: 1" + System.lineSeparator()
            + "Name: test stop 2" + System.lineSeparator()
            + "Position: 44.97358,-93.235071" + System.lineSeparator()
            + "****Passengers Info Start****" + System.lineSeparator()
            + "Num passengers waiting: 0" + System.lineSeparator()
            + "****Passengers Info End****" + System.lineSeparator()
            + "####Stop Info End####" + System.lineSeparator()
            + "####Stop Info Start####" + System.lineSeparator()
            + "ID: 2" + System.lineSeparator()
            + "Name: test stop 3" + System.lineSeparator()
            + "Position: 44.975392,-93.226632" + System.lineSeparator()
            + "****Passengers Info Start****" + System.lineSeparator()
            + "Num passengers waiting: 0" + System.lineSeparator()
            + "****Passengers Info End****" + System.lineSeparator()
            + "####Stop Info End####" + System.lineSeparator()
            + "****Stops Info End****" + System.lineSeparator()
            + "####Route Info End####" + System.lineSeparator();
    assertEquals(data, strToCompare);
  }

  /**
   * Testing functionality of isAtEnd method when it's not at the end of the route.
   */
  @Test
  void testIsAtEndFalse() {
    boolean result = testRoute.isAtEnd();
    assertFalse(result);
  }

  /**
   * Testing functionality of isAtEnd method when it's at the end of the route.
   */
  @Test
  void testIsAtEndTrue() {
    testRoute.nextStop();
    testRoute.nextStop();
    testRoute.nextStop();
    boolean result = testRoute.isAtEnd();
    assertTrue(result);
  }

  /**
   * Testing functionality of prevStop method.
   */
  @Test
  void testPrevStop() {
    assertTrue(testRoute.prevStop().equals(testRoute.getStops().get(0)));
  }

  /**
   * Testing functionality of nextStop method.
   */
  @Test
  void testNextStop() {
    testRoute.nextStop();
    assertTrue(testRoute.getStops().get(1).equals(testRoute.getNextStop()));
  }

  /**
   * Testing functionality of generateNewPassengers method.
   */
  @Test
  void testGenerateNewPassengers() {
    int result = testRoute.generateNewPassengers();
    assertEquals(2, result);
    assertEquals(2, PassengerFactory.DETERMINISTIC_NAMES_COUNT);
  }

}