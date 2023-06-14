package com.enactor.bus.model;

public enum RouteCode {

	AB("AB"),
	AC("AC"),
	AD("AD"),
	BC("BC"),
	BD("BD"),
	CD("CD"),
	DC("DC"),
	DB("BA"),
	DA("DA"),
	CB("CB"),
	CA("CA"),
	BA("BA");
	
	public final String label;

    private RouteCode(String label) {
        this.label = label;
    }
}
