package com.qumla.service.test;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qumla.domain.user.Session;
import com.qumla.service.impl.LocationDaoMapper;
import com.qumla.service.impl.LocationServiceImpl;
import com.qumla.service.impl.LoginDaoMapper;
import com.qumla.util.GeolocationUtil;

public class GeolocationTest extends AbstractTest{

	@Before
	public void before() {
		super.before(LoginDaoMapper.class);
	}
	
	public void locationTest(){
		LoginDaoMapper mapper=(LoginDaoMapper)getMapper(LoginDaoMapper.class);
		Session session = mapper.getSession("testtoken");
		
		GeolocationUtil util = createLocationservice();
		try {
			util.updateSessionLocation(session, "89.132.144.202");
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
	private GeolocationUtil createLocationservice() {
		LocationDaoMapper locationMapper=(LocationDaoMapper)getMapper(LocationDaoMapper.class);
		LocationServiceImpl lsimpl=new LocationServiceImpl();
		lsimpl.setLocationDaoMapper(locationMapper);
		GeolocationUtil util= new GeolocationUtil(lsimpl);
		return util;
	}
	
	public void updateSessionsWithoutLocation() throws Exception{
		beforeClass();
		before();
		GeolocationUtil util = createLocationservice();
		LoginDaoMapper mapper=(LoginDaoMapper)getMapper(LoginDaoMapper.class);
		List<Session> sessionList=mapper.getSessionWithoutLocation();
		for (Session session : sessionList) {
			
			try {
				Thread.sleep(1000);
				util.updateSessionLocation(session, session.getRemoteIp());
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
	
	public static final void main(String args[]) throws Exception{
		new GeolocationTest().updateSessionsWithoutLocation();
	}
}
