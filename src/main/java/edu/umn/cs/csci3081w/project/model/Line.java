package edu.umn.cs.csci3081w.project.model;

public class Line {
  private Route outboundRoute;
  private Route inboundRoute;

  public Line(Route outboundRoute, Route inboundRoute) {
    this.outboundRoute = outboundRoute;
    this.inboundRoute = inboundRoute;
  }

  public Route getOutboundRoute() {
    return outboundRoute;
  }

  public Route getInboundRoute() {
    return inboundRoute;
  }
}