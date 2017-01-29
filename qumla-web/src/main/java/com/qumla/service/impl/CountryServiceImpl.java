package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.location.Country;
@Component("countryServiceImpl")
public class CountryServiceImpl implements ResourceRepository<Country, Long>{
	@Autowired
	private LocationDataDaoMapper locationDataDaoMapper;
	
	@Override
	public void delete(Long arg0) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
		
	}

	@Override
	public Iterable<Country> findAll(Iterable<Long> arg0, QueryParams arg1) {
		return locationDataDaoMapper.findAllCountry();
	}

	@Override
	public Iterable<Country> findAll(QueryParams arg0) {
		return locationDataDaoMapper.findAllCountry();
	}

	@Override
	public Country findOne(Long arg0, QueryParams arg1) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends Country> S save(S arg0) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public LocationDataDaoMapper getLocationDataDaoMapper() {
		return locationDataDaoMapper;
	}

	public void setLocationDataDaoMapper(LocationDataDaoMapper locationDataDaoMapper) {
		this.locationDataDaoMapper = locationDataDaoMapper;
	}

}
