package com.enactor.bus.service;

import java.util.List;

import com.enactor.bus.model.BusStop;
import com.enactor.bus.model.Reservation;
import com.enactor.bus.model.ReservationDetails;
import com.enactor.bus.model.Route;
import com.enactor.bus.model.SeatCode;

public interface ReservationService {

	/**
	 * Get reservation availability details
	 * 
	 * @param reservationDetails
	 * @return
	 */
	List<SeatCode> reservationAvailability(ReservationDetails reservationDetails);
	
	/**
	 * Make Reservation
	 * 
	 * @param reservationDetails
	 * @return
	 */
	Reservation makeReservation(ReservationDetails reservationDetails);

	/**
	 * Get Bus Stop by it's name
	 * 
	 * @param origin
	 * @return
	 */
	BusStop getBusStopByName(String origin);

	/**
	 * Get route by origin and destination
	 * 
	 * @param origin
	 * @param destination
	 * @return
	 */
	Route getRoute(BusStop origin, BusStop destination);
	
	/**
	 * Get availability of the bus stop
	 * 
	 * @param busStop
	 * @return
	 */
	boolean isBusStopExists(String busStop);
}
