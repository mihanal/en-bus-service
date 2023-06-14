package com.enactor.bus.util;

import org.apache.commons.lang3.RandomUtils;

public class Helper {

	public static long generateReservationId() {
		long lowerBound = 100;
        long upperBound = 100000;
		return RandomUtils.nextLong(lowerBound, upperBound);
	}
}
