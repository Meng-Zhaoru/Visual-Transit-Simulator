package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusTest {

  private Bus testBus;
  private Route testRouteOut;
  private Route testRouteIn;

  /**
   * Setup operations before each test.
   */
  @BeforeEach
  void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
    testBus = createBus();
    testRouteOut = createOutBoundRoute();
    testRouteIn = createInBoundRoute();
  }

  /**
   * Create outbound route.
   */
  public Route createOutBoundRoute() {
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    Stop stop3 = new Stop(2, "test stop 2", new Position(-93.226632, 44.975392));
    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop1);
    stopsOut.add(stop2);
    stopsOut.add(stop3);
    List<Double> distancesOut = new ArrayList<Double>();
    distancesOut.add(0.9712663713083954);
    distancesOut.add(0.961379387775189);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(.15);
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.0);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(stopsOut, probabilitiesOut);
    return new Route(10, "testLine", "BUS", "testRouteOut",
        stopsOut, distancesOut, generatorOut);
  }

  /**
   * Create inbound route.
   */
  public Route createInBoundRoute() {
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    Stop stop3 = new Stop(2, "test stop 2", new Position(-93.226632, 44.975392));
    List<Stop> stopsIn = new ArrayList<>();
    stopsIn.add(stop3);
    stopsIn.add(stop2);
    stopsIn.add(stop1);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.961379387775189);
    distancesIn.add(0.9712663713083954);
    List<Double> probabilitiesIn = new ArrayList<>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    probabilitiesIn.add(.0);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(stopsIn, probabilitiesIn);
    return new Route(11, "testLine", "BUS", "testRouteIn",
        stopsIn, distancesIn, generatorIn);
  }

  /**
   * Create a bus with outgoing and incoming routes and three stops per route.
   */
  public Bus createBus() {
    Route testRouteOut = createOutBoundRoute();
    Route testRouteIn = createInBoundRoute();
    Line testLine = new Line(testRouteOut, testRouteIn);
    return new Bus(0, testLine, 5, 1);
  }

  /**
   * test the state after using constructor.
   */
  @Test
  void testConstructorNormal() {
    assertEquals(0, testBus.getId());
    assertTrue(testBus.getLine().getOutboundRoute().equals(testRouteOut));
    assertTrue(testBus.getLine().getInboundRoute().equals(testRouteIn));
    assertEquals(0, testBus.getDistanceRemaining());
    assertTrue(testBus.getNextStop().equals(testRouteOut.getNextStop()));
    assertEquals(5, testBus.getCapacity());
    assertEquals(1, testBus.getSpeed());
  }

  /**
   * Test the functionality of isTripComplete method when the trip doesn't finish.
   */
  @Test
  void testMoveIsTripCompleteFalse() {
    testBus.move();
    String newStop = testBus.getNextStop().getName();
    assertEquals("test stop 2", newStop);
  }

  /**
   * test move method when trip is complete.
   */
  @Test
  void testMoveIsTripCompleteTrue() {
    // Setup
    testBus.move();
    testBus.move();
    testBus.move();
    testBus.move();
    testBus.move();
    testBus.move();
    Position positionBeforeTripComplete = testBus.getPosition();
    // Run the test
    testBus.move();
    Position positionAfterTripComplete = testBus.getPosition();
    // Verify the results
    assertEquals(positionBeforeTripComplete, positionAfterTripComplete);
  }

  /**
   * Test the functionality of isTripComplete when finishes the trip.
   */
  @Test
  void testIsTripCompleteTrue() {
    // Setup
    testBus.move();
    testBus.move();
    testBus.move();
    testBus.move();
    testBus.move();
    testBus.move();
    testBus.move();
    // Run the test
    final boolean result = testBus.isTripComplete();

    // Verify the results
    assertTrue(result);
  }

  /**
   * Test if loadPassenger method successfully loads a passenger.
   */
  @Test
  void testLoadPassenger() {
    // Setup
    final Passenger newPassenger = new Passenger(0, "name");

    // Run the test
    final int result = testBus.loadPassenger(newPassenger);

    // Verify the results
    assertEquals(1, result);
  }

  /**
   * Test reporting functionality.
   */
  @Test
  void testReportWithPassenger() throws Exception {
    // Setup
    testBus.setName("testBus");
    Passenger passenger = new Passenger(1, "Kobe Bryant");
    testBus.loadPassenger(passenger);
    // Run the test
    final Charset charset = StandardCharsets.UTF_8;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream testStream = new PrintStream(outputStream, true, charset.name());
    testBus.report(testStream);
    outputStream.flush();
    String data = outputStream.toString(charset);
    testStream.close();
    outputStream.close();
    String strToCompare =
        "####Bus Info Start####" + System.lineSeparator()
            + "ID: 0" + System.lineSeparator()
            + "Name: testBus" + System.lineSeparator()
            + "Speed: 1.0" + System.lineSeparator()
            + "Capacity: 5" + System.lineSeparator()
            + "Position: 44.972392,-93.243774" + System.lineSeparator()
            + "Distance to next stop: 0.0" + System.lineSeparator()
            + "****Passengers Info Start****" + System.lineSeparator()
            + "Num of passengers: 1" + System.lineSeparator()
            + "####Passenger Info Start####" + System.lineSeparator()
            + "Name: Kobe Bryant" + System.lineSeparator()
            + "Destination: 1" + System.lineSeparator()
            + "Wait at stop: 0" + System.lineSeparator()
            + "Time on vehicle: 1" + System.lineSeparator()
            + "####Passenger Info End####" + System.lineSeparator()
            + "****Passengers Info End****" + System.lineSeparator()
            + "####Bus Info End####" + System.lineSeparator();

    // Verify the results
    assertEquals(data, strToCompare);
  }

  /**
   * Test the state of nextStop after using update method.
   */
  @Test
  void testUpdate() {
    testBus.update();
    String result = testBus.getNextStop().getName();
    assertEquals("test stop 2", result);
  }

  /**
   * Test the functionality of getNextStop method.
   */
  @Test
  void getNextStop() {
    String result = testBus.getNextStop().getName();
    assertEquals("test stop 1", result);
  }
}