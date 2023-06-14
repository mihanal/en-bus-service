package com.enactor.bus.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Reservation {

	private Long reservationId;
	private int passengerCount;
	private String origin;
	private String destination;
	private double amount;
	private List<String> seatCodes;
	private String arrival;
	private String departure;
}
