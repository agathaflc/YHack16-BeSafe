package com.theark.emergency;

public class Hospital {

	String name = "";
	String vicinity = "";
	String reference = "";
	String phone_number = "";
	String latitude = "";
	String longitude = "";

	public Hospital() {
		super();
		this.name = "";
		this.vicinity = "";
		this.reference = "";
		this.phone_number = "";
		this.latitude = "";
		this.longitude = "";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVicinity() {
		return vicinity;
	}
	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	
}
