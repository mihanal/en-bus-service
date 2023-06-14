package com.enactor.bus.model;

public enum SeatCode {

	S1A("1A"),
	S1B("1B"),
	S1C("1C"),
	S1D("1D"),
	S2A("2A"),
	S2B("2B"),
	S2C("2C"),
	S2D("2D"),
	S3A("3A"),
	S3B("3B"),
	S3C("3C"),
	S3D("3D"),
	S4A("4A"),
	S4B("4B"),
	S4C("4C"),
	S4D("4D"),
	S5A("5A"),
	S5B("5B"),
	S5C("5C"),
	S5D("5D"),
	S6A("6A"),
	S6B("6B"),
	S6C("6C"),
	S6D("6D"),
	S7A("7A"),
	S7B("7B"),
	S7C("7C"),
	S7D("7D"),
	S8A("8A"),
	S8B("8B"),
	S8C("8C"),
	S8D("8D"),
	S9A("9A"),
	S9B("9B"),
	S9C("9C"),
	S9D("9D"),
	S10A("10A"),
	S10B("10B"),
	S10C("10C"),
	S10D("10D");
	
	public final String label;

    private SeatCode(String label) {
        this.label = label;
    }
}
