package com.enactor.bus.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.enactor.bus.dao.ReservationDAO;
import com.enactor.bus.dto.ReservationDTO;
import com.enactor.bus.dto.ReservationDetailsDTO;
import com.enactor.bus.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServletTest extends Mockito {

	@Mock
	ReservationService reservationService;

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Before
	public void loadData( ) {
		ReservationDAO.loadData();
	}

	@Test
	public void testdoGet() throws Exception {

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		String requestBody = "{ \"origin\" : \"D\", \"destination\" : \"B\", \"numberOfPassengers\" : 14 }";

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		when(request.getInputStream()).thenReturn(getStream(requestBody));
		when(response.getWriter()).thenReturn(pw);

		new ReservationServlet().doGet(request, response);

		ReservationDetailsDTO reservationDetailsDTO = objectMapper.readValue(sw.getBuffer().toString(),
				ReservationDetailsDTO.class);
		assertEquals(true, reservationDetailsDTO.isAvailable());
	}
	
	@Test
	public void testdoPost() throws Exception {

		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		String requestBody = "{ \"origin\" : \"D\", \"destination\" : \"B\", \"numberOfPassengers\" : 4, \"amount\" : 1200.00 }";

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		when(request.getInputStream()).thenReturn(getStream(requestBody));
		when(response.getWriter()).thenReturn(pw);

		new ReservationServlet().doPost(request, response);

		ReservationDTO reservationDTO = objectMapper.readValue(sw.getBuffer().toString(),
				ReservationDTO.class);
		assertNotNull(reservationDTO.getReservationId());
	}

	private ServletInputStream getStream(String inputString) {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				inputString.getBytes(StandardCharsets.UTF_8));
		return new ServletInputStream() {
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
			}
		};
	}
}
