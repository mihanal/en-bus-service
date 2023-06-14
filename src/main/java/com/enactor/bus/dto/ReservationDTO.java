package com.enactor.bus.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDTO {

	private Long reservationId;
	private int passengerCount;
	private String origin;
	private String destination;
	private double amount;
	private List<String> seatCodes;
	private String departure;
	private String arrival;
}
