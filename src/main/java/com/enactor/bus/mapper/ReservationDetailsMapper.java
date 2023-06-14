package com.enactor.bus.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.enactor.bus.dto.ReservationDetailsDTO;
import com.enactor.bus.model.ReservationDetails;

@Mapper
public interface ReservationDetailsMapper {

	ReservationDetailsMapper INSTANCE = Mappers.getMapper(ReservationDetailsMapper.class);
	
	@Mapping(source = "reservationDetails.available", target = "isAvailable")
	ReservationDetailsDTO toReservationDetailsDTO(ReservationDetails reservationDetails);
	
	@Mapping(source = "reservationDetailsDTO.available", target = "isAvailable")
	ReservationDetails toReservationDetails(ReservationDetailsDTO reservationDetailsDTO);
}
