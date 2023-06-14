package com.enactor.bus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetails {

	private boolean isAvailable;
	private String origin;
	private String destination;
	private int numberOfPassengers;
	private double amount;
}
