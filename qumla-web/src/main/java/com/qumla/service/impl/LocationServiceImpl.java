package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.location.Location;
import com.qumla.service.LocationService;
@Component("locationServiceImpl")
public class LocationServiceImpl implements ResourceRepository<Location, Long>, LocationService{
	
	@Autowired
	private LocationDaoMapper locationDaoMapper;
	
	
	@Override
	public Location findOne(Long id, QueryParams requestParams) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
	}

	@Override
	public Iterable<Location> findAll(QueryParams requestParams) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
	}

	@Override
	public Iterable<Location> findAll(Iterable<Long> ids,
			QueryParams requestParams) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
	}

	@Override
	public <S extends Location> S save(S entity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
		
	}


	@Override
	public Location findByCity(String country, String name) {
		return locationDaoMapper.findByCity(country, name);
	}


	@Override
	public Location findByCityAndZip(String country, String name, String zip) {
		return locationDaoMapper.findByCityAndZip(country, name, zip);		
	}


	@Override
	public void insertLocation(Location location) {
		locationDaoMapper.insertLocation(location);
	}


	@Override
	public void updateLocation(Location location) {
		locationDaoMapper.updateLocation(location);
	}

	public void setLocationDaoMapper(LocationDaoMapper locationDaoMapper) {
		this.locationDaoMapper = locationDaoMapper;
	}

}
