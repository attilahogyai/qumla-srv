package com.qumla.service.impl;

import org.apache.ibatis.annotations.Param;

import com.qumla.domain.location.Location;

public interface LocationDaoMapper {
	public Location findOne(Long id);
	public Location findByCity(@Param("country") String country,@Param("city") String name);
	public Location findByCityAndZip(@Param("country") String country,@Param("city") String name,@Param("zip") String zip);
	public void insertLocation(Location location);
	public void updateLocation(Location location);
	
	
}
