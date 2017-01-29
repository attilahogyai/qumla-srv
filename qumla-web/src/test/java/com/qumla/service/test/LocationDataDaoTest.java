package com.qumla.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.qumla.domain.location.Country;
import com.qumla.domain.location.LocationData;
import com.qumla.service.impl.LocationDataDaoMapper;


public class LocationDataDaoTest extends AbstractTest {
	@Before
	public void before() {
		super.before(LocationDataDaoMapper.class);	
	}
	@Test
	public void locationTest(){
		LocationDataDaoMapper mapper=(LocationDataDaoMapper)getMapper(LocationDataDaoMapper.class);
		LocationData location=mapper.findByName("HU","Budapest");
		assertTrue(location.getName().equals("Budapest"));
	}
	@Test
	public void insertLocationTest(){
		LocationData l=new LocationData();
		l.setCity(11L);
		l.setCountry("HU");
		l.setLat(new Float(47.7));
		l.setLon(new Float(35.2));
		
		LocationDataDaoMapper mapper=(LocationDataDaoMapper)getMapper(LocationDataDaoMapper.class);
		mapper.insertLocation(l);
		
		LocationData l2=mapper.findOne(l.getId());
		assertTrue(l2.getCity().equals(11L));
		assertEquals("HU", l2.getCountry());
		assertEquals(new Float(47.7), l2.getLat());
		assertEquals(new Float(35.2), l2.getLon());
	}
	@Test
	public void findCountry(){
		LocationDataDaoMapper mapper=(LocationDataDaoMapper)getMapper(LocationDataDaoMapper.class);
		Country c=mapper.findCountry("HU");
		assertTrue(c.getCountryName().equals("Hungary"));
	}
	
	
}
