package com.qumla.service;

import com.qumla.domain.location.Location;

public interface LocationService {
	public Location findByCity(String country, String name);
	public Location findByCityAndZip(String country, String name,String zip);
	public void insertLocation(Location location);
	public void updateLocation(Location location);
}
