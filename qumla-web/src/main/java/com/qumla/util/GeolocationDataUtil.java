package com.qumla.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.cert.CertificateException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.HtmlUtils;

import se.walkercrou.places.AddressComponent;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qumla.domain.location.Location;
import com.qumla.domain.location.LocationData;
import com.qumla.domain.user.Session;
import com.qumla.service.LocationDataService;

public class GeolocationDataUtil {

	@Autowired
	@Qualifier("locationDataServiceImpl")
	private LocationDataService locationDataService;

	private static final Logger log = LoggerFactory
			.getLogger(GeolocationUtil.class);
	//public static final String googleAPIKey = "AIzaSyB5zaeF81zrW1sZwPwfF7CSFhZ3c7NU9LI";

	private CloseableHttpClient httpClient = HttpClients.createMinimal();
	private CloseableHttpClient httpsClient = null;

	private SSLConnectionSocketFactory sslsf = null;
	GooglePlaces googleLocationClient = new GooglePlaces(Constants.GOOGLE_API_KEY, new PlacesRequestHandler());

	public GeolocationDataUtil(LocationDataService locationService) {
		this.locationDataService = locationService;
		try {
			sslsf = new SSLConnectionSocketFactory(createSSLContext(),
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		} catch (KeyManagementException e) {
			log.error("KeyManagementException", e);
		}
		httpsClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
	}

	private SSLContext createSSLContext() throws KeyManagementException {
		SSLContext sslcontext = SSLContexts.createDefault();
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			@Override
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] arg0, String arg1)
					throws CertificateException {
			}

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		} };
		sslcontext.init(null, trustAllCerts, new java.security.SecureRandom());
		return sslcontext;
	}

	public void getIpGeoplugin(final String ip, Session session)
			throws IOException, ClientProtocolException,
			JsonProcessingException {

		HttpGet rq = new HttpGet("http://www.geoplugin.net/json.gp?ip=" + ip);
		HttpResponse resp = httpClient.execute(rq);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = objectMapper.readTree(resp.getEntity().getContent());
		JsonNode countryCode = node.get("geoplugin_countryCode");
		if (countryCode != null && countryCode.asText() != null
				&& !countryCode.asText().equals("")) {
			session.setCountry(countryCode.asText());
		}
		JsonNode cityName = node.get("geoplugin_city");
		if (cityName != null && cityName.asText() != null
				&& !cityName.asText().equals("")) {
			session.setCity(cityName.asText());
		}
		JsonNode latitude = node.get("geoplugin_latitude");
		if (latitude != null && latitude.asText() != null
				&& !latitude.asText().equals("")) {
			session.setLat(Float.parseFloat(latitude.asText()));
		}
		JsonNode longitude = node.get("geoplugin_longitude");
		if (longitude != null && longitude.asText() != null
				&& !longitude.asText().equals("")) {
			session.setLon(Float.parseFloat(longitude.asText()));
		}
	}

	public void getfreegeoip(final String ip, Session session)
			throws IOException, ClientProtocolException,
			JsonProcessingException {
		HttpGet rq = new HttpGet("https://freegeoip.net/json/" + ip);
		HttpResponse resp = httpsClient.execute(rq);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = objectMapper.readTree(resp.getEntity().getContent());
		JsonNode countryCode = node.get("country_code");
		if (countryCode != null && countryCode.asText() != null
				&& !countryCode.asText().equals("")) {
			session.setCountry(countryCode.asText());
		}
		JsonNode cityName = node.get("city");
		if (cityName != null && cityName.asText() != null
				&& !cityName.asText().equals("")) {
			session.setCity(cityName.asText());
		}
		JsonNode latitude = node.get("latitude");
		if (latitude != null && latitude.asText() != null
				&& !latitude.asText().equals("")) {
			session.setLat(Float.parseFloat(latitude.asText()));
		}
		JsonNode longitude = node.get("longitude");
		if (longitude != null && longitude.asText() != null
				&& !longitude.asText().equals("")) {
			session.setLon(Float.parseFloat(longitude.asText()));
		}
	}

	public void getIpApi(final String ip, Session session)
			throws IOException, ClientProtocolException,
			JsonProcessingException {
		HttpGet rq = new HttpGet("http://ip-api.com/json/" + ip);
		HttpResponse resp = httpClient.execute(rq);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = objectMapper.readTree(resp.getEntity().getContent());
		JsonNode countryCode = node.get("countryCode");
		if (countryCode != null && countryCode.asText() != null
				&& !countryCode.asText().equals("")) {
			session.setCountry(countryCode.asText());
		}
		JsonNode cityName = node.get("city");
		if (cityName != null && cityName.asText() != null
				&& !cityName.asText().equals("")) {
			session.setCity(cityName.asText());
		}
		JsonNode latitude = node.get("lat");
		if (latitude != null && latitude.asText() != null
				&& !latitude.asText().equals("")) {
			session.setLat(Float.parseFloat(latitude.asText()));
		}
		JsonNode longitude = node.get("lon");
		if (longitude != null && longitude.asText() != null
				&& !longitude.asText().equals("")) {
			session.setLon(Float.parseFloat(longitude.asText()));
		}
	}

	public void setGeolocation(Location location) throws ClientProtocolException, IOException{
		HttpGet rq = new HttpGet("https://maps.googleapis.com/maps/api/geocode/json?address="+HttpHelper.urlEncode(location.getCityName())+"&key=" + Constants.GOOGLE_API_KEY);
		HttpResponse resp = httpsClient.execute(rq);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = objectMapper.readTree(resp.getEntity().getContent());
		String status=node.get("status")!=null?node.get("status").asText():null;
		if(status!=null && status.equals("OK")){
			try{
				JsonNode locationNode=node.get("results").get(0).get("geometry").get("location");
				String lat=locationNode.get("lat").asText();
				String lon=locationNode.get("lng").asText();
				location.setLat(Float.parseFloat(lat));
				location.setLon(Float.parseFloat(lon));
			}catch(Exception e){
				log.error("UNABLE TO SET GEO for LOCATION:"+node,e);
			}
		}
	}
	
	public void updateCountryCity(Session session, String ip) throws ClientProtocolException, JsonProcessingException, IOException{
		if(ip.equals("127.0.0.1") || ip.equals("localhost")){
			ip="89.133.26.180";
		}
		getIpApi(ip, session);
		if (session.getCountry() == null || session.getCity() == null) {
			log.error("!!! UNABLE TO GET LOCATION FROM ip-api.com");
			getIpGeoplugin(ip, session);
		}
		if (session.getCountry() == null || session.getCity() == null) {
			log.warn("---- UNABLE TO SET COUNTRY[" + session.getCountry()
					+ "] OR CITY[" + session.getCity() + "] FOR[" + ip
					+ "]----");
		}
	}
	public void updateSessionLocation(Session session)
			throws IOException, ClientProtocolException,
			JsonProcessingException {
		// try to find and update location
		String name = HtmlUtils.htmlUnescape(session.getCity());
		
		LocationData location=getLocationDetail(name,session.getCountry(),new HashSet<String>());
		if(location!=null){
			System.out.println("set location: "+location.getId()+" for :"+session.getRemoteIp()+" - "+session.getCountry()+", "+session.getCity());
			session.setLocation(location.getId());
		}else{
			System.out.println("unable to set location for :"+session.getRemoteIp()+" - "+session.getCountry()+", "+session.getCity());			
		}
	}

	private LocationData getLocationDetail(String name,String country,Set<String> matches) {
		if(name==null || name.equals("[no name]")) return null;
		LocationData location = locationDataService.findByName(country, name);
		if(location==null){
			List<Place> places = googleLocationClient.getPlacesByQuery(name+", "+country, GooglePlaces.MAXIMUM_RESULTS);
			for (Place place : places) {
				if(matches.contains(place.getAddress())) {
					return null;
				}else{
					matches.add(place.getAddress());
				}
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
			if(location==null){
				log.warn("LOCATION NOT FOUND for : "+name+" ("+country+")");
				return null;
			}
			locationDataService.insertLocation(location);
			if(location.getCityName()!=null){
				LocationData cityLocation=getLocationDetail(location.getCityName(),country, matches);
				if(cityLocation!=null){
					location.setCity(cityLocation.getId());
				}
			}
			if(location.getPoliticalArea1Name()!=null){
				LocationData pa1=getLocationDetail(location.getPoliticalArea1Name(),country, matches);
				if(pa1!=null){
					location.setPoliticalArea1(pa1.getId());
				}
			}
			if(location.getPoliticalArea2Name()!=null){
				LocationData pa2=getLocationDetail(location.getPoliticalArea2Name(),country, matches);
				if(pa2!=null){
					location.setPoliticalArea2(pa2.getId());
				}
			}			
			locationDataService.updateLocation(location);			
		}
		return location;
	}
}
