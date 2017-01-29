package com.qumla.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.cert.CertificateException;

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
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qumla.domain.location.Location;
import com.qumla.domain.user.Session;
import com.qumla.service.LocationService;

public class GeolocationUtil {

	private LocationService locationService;

	private static final Logger log = LoggerFactory
			.getLogger(GeolocationUtil.class);
	public static final String googleAPIKey = "AIzaSyB5zaeF81zrW1sZwPwfF7CSFhZ3c7NU9LI";

	private CloseableHttpClient httpClient = HttpClients.createMinimal();
	private CloseableHttpClient httpsClient = null;

	private SSLConnectionSocketFactory sslsf = null;

	public GeolocationUtil(LocationService locationService) {
		this.locationService = locationService;
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
		HttpGet rq = new HttpGet("https://maps.googleapis.com/maps/api/geocode/json?address="+HttpHelper.urlEncode(location.getCityName())+"&key=" + googleAPIKey);
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

	public void updateSessionLocation(Session session, final String ip)
			throws IOException, ClientProtocolException,
			JsonProcessingException {

		getIpApi(ip, session);
		if (session.getCountry() == null || session.getCity() == null) {
			log.error("!!! UNABLE TO GET LOCATION FROM ip-api.com");
			getIpGeoplugin(ip, session);
		}
		if (session.getCountry() == null || session.getCity() == null) {
			log.warn("---- UNABLE TO SET COUNTRY[" + session.getCountry()
					+ "] OR CITY[" + session.getCity() + "] FOR[" + ip
					+ "]----");
		} else {
			// try to find and update location
			String city = HtmlUtils.htmlUnescape(session.getCity());
			Location location = locationService.findByCity(
					session.getCountry(), city);
			if (location == null) {
				location = new Location();
				location.setCityName(city);
				location.setCountryCode(session.getCountry());
				locationService.insertLocation(location);
			}
			if (location.getLat() == null) {
				setGeolocation(location);
				locationService.updateLocation(location);
			}
			// find city geolocation from google
			session.setLocation(location.getId());
		}
	}
}
