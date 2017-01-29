package com.qumla.service;

import com.qumla.domain.location.LocationData;

public interface LocationDataService {
	public LocationData findByCity(String country, String name);
	public LocationData findByName(String country, String name);
	
	public void insertLocation(LocationData location);
	public void updateLocation(LocationData location);
}
