package com.enactor.bus.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Seat {

	private SeatCode seatCode;
	private List<Reservation> reservations;
}
