package com.qumla.service.impl;

import org.apache.ibatis.annotations.Param;

import com.qumla.domain.location.Country;
import com.qumla.domain.location.LocationData;

public interface LocationDataDaoMapper {
	public LocationData findOne(Long id);
	public LocationData findByCity(@Param("country") String country,@Param("city") String name);
	public LocationData findByName(@Param("country") String country,@Param("name") String name);

	public Country findCountry(@Param("code") String code);
	public java.util.List<Country> findAllCountry();

	
	
	public void insertLocation(LocationData location);
	public void updateLocation(LocationData location);

	public void updateCountryQuestionCount(Country location);
	public void incrementCountryQuestionCount(Country location);

	
}
