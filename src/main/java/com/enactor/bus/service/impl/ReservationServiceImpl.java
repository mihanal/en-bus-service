package com.enactor.bus.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ObjectUtils;

import com.enactor.bus.dao.ReservationDAO;
import com.enactor.bus.exception.CustomException;
import com.enactor.bus.model.BusStop;
import com.enactor.bus.model.Reservation;
import com.enactor.bus.model.ReservationDetails;
import com.enactor.bus.model.Route;
import com.enactor.bus.model.Seat;
import com.enactor.bus.model.SeatCode;
import com.enactor.bus.service.ReservationService;
import com.enactor.bus.util.Helper;

public class ReservationServiceImpl implements ReservationService {

	@Override
	public List<SeatCode> reservationAvailability(ReservationDetails reservationDetails) {
		BusStop origin = getBusStopByName(reservationDetails.getOrigin());
		BusStop destination = getBusStopByName(reservationDetails.getDestination());

		int originIndex = getIndex(origin.getName());
		int destinationIndex = getIndex(destination.getName());

		List<Seat> availableAllocation = ReservationDAO.seatAllocation
				.stream()
				.filter(st -> {
					if (ObjectUtils.isEmpty(st.getReservations())) {
						return true;
					} else {
						List<Reservation> availableReservation = st.getReservations()
								.stream()
								.filter(doFilterSeatAvailability(originIndex, destinationIndex))
								.collect(Collectors.toList());
						return availableReservation.isEmpty();
					}
				}).collect(Collectors.toList());

		List<SeatCode> availableSeats = availableAllocation.stream().map(st -> st.getSeatCode())
				.collect(Collectors.toList());

		if (availableSeats.size() < reservationDetails.getNumberOfPassengers()) {
			throw new CustomException("No available seats");
		}

		return availableSeats;
	}

	@Override
	public Reservation makeReservation(ReservationDetails reservationDetails) {
		BusStop origin = getBusStopByName(reservationDetails.getOrigin());
		BusStop destination = getBusStopByName(reservationDetails.getDestination());

		List<SeatCode> availability = reservationAvailability(reservationDetails);

		Reservation reservation = Reservation.builder().origin(origin.getName()).destination(destination.getName())
				.build();

		synchronized (ReservationDAO.seatAllocation) {
			List<SeatCode> bookedSeatCodes = availability.stream().limit(reservationDetails.getNumberOfPassengers())
					.collect(Collectors.toList());

			Route route = getRoute(origin, destination);

			double price = route.getPrice() * reservationDetails.getNumberOfPassengers();
			if (price > reservationDetails.getAmount()) {
				throw new CustomException("Reservation total amount greater than provided");
			}

			long reservationId = Helper.generateReservationId();

			reservation.setReservationId(reservationId);
			reservation.setAmount(price);
			reservation.setPassengerCount(reservationDetails.getNumberOfPassengers());
			reservation.setArrival(route.getArrival());
			reservation.setDeparture(route.getDeparture());

			List<String> seats = bookedSeatCodes.stream().map(st -> st.label).collect(Collectors.toList());
			reservation.setSeatCodes(seats);

			// update seat with reservation details
			bookedSeatCodes.stream().forEach(code -> {
				ReservationDAO.seatAllocation
					.stream()
					.filter(st -> st.getSeatCode().equals(code)).map(st -> {
						if (st.getReservations() == null) {
							List<Reservation> reservations = new ArrayList<>();
							reservations.add(reservation);
							st.setReservations(reservations);
						} else {
							st.getReservations().add(reservation);
						}
						return st;
					})
					.collect(Collectors.toList());
			});
		}
		return reservation;
	}

	@Override
	public Route getRoute(BusStop origin, BusStop destination) {
		return ReservationDAO.routes.stream()
				.filter(route -> route.getOrigin().getName().equals(origin.getName())
						&& route.getDestination().getName().equals(destination.getName()))
				.findFirst()
				.get();
	}

	@Override
	public BusStop getBusStopByName(String name) {
		return ReservationDAO.busStops
				.stream()
				.filter(s -> s.getName().equals(name))
				.findFirst()
				.get();
	}

	public int getIndex(String busStop) {
		return IntStream.range(0, ReservationDAO.busStops.size())
				.filter(i -> ReservationDAO.busStops.get(i).getName().equals(busStop))
				.findFirst()
				.orElse(-1);
	}

	private Predicate<Reservation> doFilterSeatAvailability(int originIndex, int destinationIndex) {
		return res -> {
			// extra condition for return
			if (originIndex < destinationIndex) {
				if (getIndex(res.getOrigin()) >= destinationIndex || getIndex(res.getDestination()) <= originIndex)
					return false;
				return true;
			} else {
				if (getIndex(res.getOrigin()) <= destinationIndex || getIndex(res.getDestination()) >= originIndex)
					return false;
				return true;
			}
		};
	}

	@Override
	public boolean isBusStopExists(String busStop) {
		return ReservationDAO.busStops
				.stream()
				.filter(stop -> stop.getName().equals(busStop))
				.count() != 0;
	}
}
