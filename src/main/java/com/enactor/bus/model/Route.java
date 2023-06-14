package com.enactor.bus.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Route {

	private double price;
	private RouteCode routeCode;
	private BusStop origin;
	private BusStop destination;
	private String arrival;
	private String departure;
}
