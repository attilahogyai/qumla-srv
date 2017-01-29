package com.qumla.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;

import com.qumla.domain.location.LocationData;
import com.qumla.service.LocationDataService;
@Component("locationDataServiceImpl")
public class LocationDataServiceImpl implements ResourceRepository<LocationData, Long>, LocationDataService{
	@Autowired
	private LocationDataDaoMapper locationDataMapper;
	
	@Override
	public LocationData findByCity(String country, String name) {
		return locationDataMapper.findByCity(country, name);
	}


	@Override
	public void insertLocation(LocationData location) {
		locationDataMapper.insertLocation(location);
		
	}

	@Override
	public void updateLocation(LocationData location) {
		locationDataMapper.updateLocation(location);
		
		
	}

	@Override
	public LocationData findOne(Long id, QueryParams queryParams) {
		return locationDataMapper.findOne(id);
	}

	@Override
	public Iterable<LocationData> findAll(QueryParams queryParams) {
		throw new UnsupportedOperationException();		
	}

	@Override
	public Iterable<LocationData> findAll(Iterable<Long> ids, QueryParams queryParams) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends LocationData> S save(S entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Long id) {
		throw new UnsupportedOperationException();
	}


	@Override
	public LocationData findByName(String country, String name) {
		return locationDataMapper.findByName(country, name);
	}


	public LocationDataDaoMapper getLocationDataMapper() {
		return locationDataMapper;
	}


	public void setLocationDataMapper(LocationDataDaoMapper locationDataMapper) {
		this.locationDataMapper = locationDataMapper;
	}

}
