package com.enactor.bus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservationDetailsDTO {

	private boolean isAvailable;
	private String origin;
	private String destination;
	private int numberOfPassengers;
	private double amount;
}
