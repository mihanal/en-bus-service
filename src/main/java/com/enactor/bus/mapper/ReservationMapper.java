package com.enactor.bus.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.enactor.bus.dto.ReservationDTO;
import com.enactor.bus.model.Reservation;

@Mapper
public interface ReservationMapper {

	ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);
	
	ReservationDTO toReservationDTO(Reservation reservation);
	
	Reservation toReservation(ReservationDTO reservationDTO);
}
