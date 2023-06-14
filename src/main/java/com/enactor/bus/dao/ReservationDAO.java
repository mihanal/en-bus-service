package com.enactor.bus.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.enactor.bus.model.BusStop;
import com.enactor.bus.model.Route;
import com.enactor.bus.model.RouteCode;
import com.enactor.bus.model.Seat;
import com.enactor.bus.model.SeatCode;

public class ReservationDAO {

	public static List<BusStop> busStops 	= new ArrayList<BusStop>();
	public static List<Route> 	routes 		= new ArrayList<Route>();
	public static List<Seat> 	seatAllocation = Collections.synchronizedList(new ArrayList<>());

	public static void loadData() {

		BusStop aStop = BusStop.builder().name("A").build();
		BusStop bStop = BusStop.builder().name("B").build();
		BusStop cStop = BusStop.builder().name("C").build();
		BusStop dStop = BusStop.builder().name("D").build();

		busStops = Arrays.asList(aStop, bStop, cStop, dStop);
		routes = Arrays.asList(
				Route.builder().routeCode(RouteCode.AB).origin(aStop).destination(bStop).price(50).departure("9.00 AM").arrival("10.00 AM").build(),
				Route.builder().routeCode(RouteCode.AC).origin(aStop).destination(cStop).price(100).departure("9.00 AM").arrival("11.00 AM").build(),
				Route.builder().routeCode(RouteCode.AD).origin(aStop).destination(dStop).price(150).departure("9.00 AM").arrival("12.00 AM").build(),
				Route.builder().routeCode(RouteCode.BC).origin(bStop).destination(cStop).price(50).departure("10.00 AM").arrival("11.00 AM").build(),
				Route.builder().routeCode(RouteCode.BD).origin(bStop).destination(dStop).price(100).departure("10.00 AM").arrival("12.00 AM").build(),
				Route.builder().routeCode(RouteCode.CD).origin(cStop).destination(dStop).price(50).departure("11.00 AM").arrival("12.00 PM").build(),
				Route.builder().routeCode(RouteCode.DA).origin(dStop).destination(aStop).price(150).departure("1.00 PM").arrival("4.00 PM").build(),
				Route.builder().routeCode(RouteCode.DB).origin(dStop).destination(bStop).price(100).departure("1.00 PM").arrival("3.00 PM").build(),
				Route.builder().routeCode(RouteCode.DC).origin(dStop).destination(cStop).price(50).departure("1.00 PM").arrival("2.00 PM").build(),
				Route.builder().routeCode(RouteCode.CA).origin(cStop).destination(aStop).price(100).departure("2.00 PM").arrival("4.00 PM").build(),
				Route.builder().routeCode(RouteCode.CB).origin(cStop).destination(bStop).price(50).departure("2.00 PM").arrival("3.00 PM").build(),
				Route.builder().routeCode(RouteCode.BA).origin(bStop).destination(aStop).price(50).departure("3.00 PM").arrival("4.00 PM").build());

		seatAllocation = Stream.of(SeatCode.values()).map(code -> Seat.builder().seatCode(code).build())
				.collect(Collectors.toList());
	}
}
