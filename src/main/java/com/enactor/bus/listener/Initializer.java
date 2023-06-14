package com.enactor.bus.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.enactor.bus.dao.ReservationDAO;

@WebListener
public class Initializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ReservationDAO.loadData();
	}
}
