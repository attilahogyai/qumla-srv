package com.qumla.service.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import se.walkercrou.places.AddressComponent;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qumla.domain.location.LocationData;
import com.qumla.domain.user.Session;
import com.qumla.service.impl.LocationDataDaoMapper;
import com.qumla.service.impl.LocationDataServiceImpl;
import com.qumla.service.impl.LoginDaoMapper;
import com.qumla.util.Constants;
import com.qumla.util.GeolocationDataUtil;
import com.qumla.util.PlacesRequestHandler;

public class GeolocationDataTest extends AbstractTest{

	@Before
	public void before() {
		super.before(LoginDaoMapper.class);
	}
	@Test
	public void locationTest(){
		LoginDaoMapper mapper=(LoginDaoMapper)getMapper(LoginDaoMapper.class);
		Session session = mapper.getSession("testtoken");
		GeolocationDataUtil util = createLocationservice();
		try {
			util.updateSessionLocation(session);
			Assert.assertEquals(session.getCity(),"Budapest");
			Assert.assertNotNull(session.getLat());
			Assert.assertNotNull(session.getLon());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	private GeolocationDataUtil createLocationservice() {
		LocationDataDaoMapper locationMapper=(LocationDataDaoMapper)getMapper(LocationDataDaoMapper.class);
		LocationDataServiceImpl lsimpl=new LocationDataServiceImpl();
		lsimpl.setLocationDataMapper(locationMapper);
		GeolocationDataUtil util= new GeolocationDataUtil(lsimpl);
		return util;
	}
	
	public void updateSessionsWithoutLocation() throws Exception{
		beforeClass();
		before();
		GeolocationDataUtil util = createLocationservice();
		LoginDaoMapper mapper=(LoginDaoMapper)getMapper(LoginDaoMapper.class);
		List<Session> sessionList=mapper.getSessionWithoutLocation();
		System.out.println("sessionList.size:"+sessionList.size());
		for (Session session : sessionList) {
			try {
				Thread.sleep(1000);
				System.out.println("update session:"+session.getCode());			
				
				util.updateSessionLocation(session);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mapper.updateSession(session);
			for (SqlSession sqlsession : sessions) {
				sqlsession.commit();
			}
		}
	}
	
	public void googleLocation(){
		GooglePlaces googleLocationClient = new GooglePlaces(Constants.GOOGLE_API_KEY, new PlacesRequestHandler());
		String country="HU";
		List<Place> places=googleLocationClient.getPlacesByQuery("God"+", "+country, GooglePlaces.MAXIMUM_RESULTS);
		LocationData location = null;
		for (Place place : places) {
			Place detail=googleLocationClient.getPlaceById(place.getPlaceId());
			location = new LocationData();
			location.setName(detail.getName());
			location.setLat((float)detail.getLatitude());
			location.setLon((float)detail.getLongitude());
			location.setGooglePlaceId(detail.getPlaceId());
			location.setType(place.getTypes().get(0));
			for (AddressComponent addressComp : detail.getAddressComponents()) {
				if(addressComp.getTypes().get(0).equals("country")){
					location.setCountry(addressComp.getShortName());
				}else if(addressComp.getTypes().get(0).equals("locality") && !location.getType().equals("locality")){
					location.setCityName(addressComp.getShortName());							
				}else if(addressComp.getTypes().get(0).equals("administrative_area_level_2") && !location.getType().equals("administrative_area_level_2")){
					location.setPoliticalArea2Name(addressComp.getShortName());
				}else if(addressComp.getTypes().get(0).equals("administrative_area_level_1") && !location.getType().equals("administrative_area_level_1")){
					location.setPoliticalArea1Name(addressComp.getShortName());
				}
			}
			if(location.getCountry().equals(country)){
				break;
			}else{
				location = null;
			}
		}
		Assert.assertEquals("Göd", location.getName());
		
	}
	
	public static final void main(String args[]) throws Exception{
		System.out.println("update null location for session");
		new GeolocationDataTest().updateSessionsWithoutLocation();
	}
}