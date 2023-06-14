package com.enactor.bus.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.enactor.bus.dto.ErrorDTO;
import com.enactor.bus.dto.ReservationDTO;
import com.enactor.bus.dto.ReservationDetailsDTO;
import com.enactor.bus.exception.CustomException;
import com.enactor.bus.mapper.ReservationDetailsMapper;
import com.enactor.bus.mapper.ReservationMapper;
import com.enactor.bus.model.BusStop;
import com.enactor.bus.model.Reservation;
import com.enactor.bus.model.ReservationDetails;
import com.enactor.bus.model.Route;
import com.enactor.bus.model.SeatCode;
import com.enactor.bus.service.ReservationService;
import com.enactor.bus.service.impl.ReservationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet(name = "ReservationServlet", urlPatterns = "/api/reservation")
public class ReservationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private ObjectMapper objectMapper = new ObjectMapper();
	private ReservationService reservationService = new ReservationServiceImpl();
	private static final Logger logger = LogManager.getLogger(ReservationServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter printWriter = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		ReservationDetailsDTO detailsDTO = null;
		try {
			detailsDTO = objectMapper.readValue(request.getInputStream(), ReservationDetailsDTO.class);
			logger.debug(detailsDTO);
			
			validateRequestInfo(detailsDTO);
			ReservationDetails details = ReservationDetailsMapper.INSTANCE.toReservationDetails(detailsDTO);
			
			// get seat availability
			List<SeatCode> seatAvailability = reservationService.reservationAvailability(details);

			ReservationDetails reservationDetails = new ReservationDetails();
			if (ObjectUtils.isNotEmpty(seatAvailability)) {
				BusStop origin = reservationService.getBusStopByName(details.getOrigin());
				BusStop destination = reservationService.getBusStopByName(details.getDestination());

				Route route = reservationService.getRoute(origin, destination);

				reservationDetails.setAvailable(true);
				reservationDetails.setAmount(route.getPrice() * details.getNumberOfPassengers());
				reservationDetails.setDestination(destination.getName());
				reservationDetails.setOrigin(origin.getName());
				reservationDetails.setNumberOfPassengers(details.getNumberOfPassengers());
			} else {
				reservationDetails.setAvailable(false);
			}

			ReservationDetailsDTO reservationDetailsDTO = ReservationDetailsMapper.INSTANCE
					.toReservationDetailsDTO(reservationDetails);

			String detailsDto = objectMapper.writeValueAsString(reservationDetailsDTO);
			printWriter.write(detailsDto);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (CustomException e) {
			detailsDTO.setAvailable(false);
			printWriter.write(objectMapper.writeValueAsString(detailsDTO));
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			ErrorDTO error = ErrorDTO.builder().message(e.getMessage()).build();
			printWriter.write(objectMapper.writeValueAsString(error));
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			printWriter.flush();
			printWriter.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter printWriter = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try {
			ReservationDetailsDTO detailsDTO = objectMapper.readValue(request.getInputStream(), ReservationDetailsDTO.class);
			logger.debug(detailsDTO);
			
			validateRequestInfo(detailsDTO);
			ReservationDetails details = ReservationDetailsMapper.INSTANCE.toReservationDetails(detailsDTO);

			// make reservation
			Reservation reservation = reservationService.makeReservation(details);
			if(ObjectUtils.isEmpty(reservation)) {
				throw new CustomException("Unable to make the reservation");
			}
			
			ReservationDTO reservationDTO = ReservationMapper.INSTANCE.toReservationDTO(reservation);

			String detailsDto = objectMapper.writeValueAsString(reservationDTO);
			printWriter.write(detailsDto);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (Exception e) {
			ErrorDTO error = ErrorDTO.builder().message(e.getMessage()).build();
			printWriter.write(objectMapper.writeValueAsString(error));
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			printWriter.flush();
			printWriter.close();
		}
	}
	
	private void validateRequestInfo(ReservationDetailsDTO detailsDTO) {
		List<String> messages = new ArrayList<>();
		
		if (!reservationService.isBusStopExists(detailsDTO.getOrigin())
				|| !reservationService.isBusStopExists(detailsDTO.getDestination())) {
			messages.add("Please check the origin and destionation");
		}
		
		if(detailsDTO.getOrigin().equals(detailsDTO.getDestination())) {
			messages.add("Origin bus stop and destination bus stop can't be same");
		}
		
		if(detailsDTO.getNumberOfPassengers() <= 0) {
			messages.add("Number of passengers should be greater than 0");
		}
		
		if(ObjectUtils.isNotEmpty(messages)) {
			throw new IllegalArgumentException(String.join(", ", messages));
		}
	}
}
