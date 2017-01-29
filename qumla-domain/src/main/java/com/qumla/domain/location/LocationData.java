package com.qumla.domain.location;

import io.katharsis.resource.annotations.JsonApiResource;

import com.qumla.domain.AbstractEntity;
@JsonApiResource(type="location_data")
public class LocationData extends AbstractEntity {
	private String name;
	private Long city;
	private String cityName;
	private String ip;
	private String country;
	private Float lon;
	private Float lat;
	private String googlePlaceId;
	private Long politicalArea1;
	private String politicalArea1Name;
	private Long politicalArea2;
	private String politicalArea2Name;
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCity() {
		return city;
	}
	public void setCity(Long city) {
		this.city = city;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Float getLon() {
		return lon;
	}
	public void setLon(Float lon) {
		this.lon = lon;
	}
	public Float getLat() {
		return lat;
	}
	public void setLat(Float lat) {
		this.lat = lat;
	}
	public String getGooglePlaceId() {
		return googlePlaceId;
	}
	public void setGooglePlaceId(String googlePlaceId) {
		this.googlePlaceId = googlePlaceId;
	}
	public Long getPoliticalArea1() {
		return politicalArea1;
	}
	public void setPoliticalArea1(Long politicalArea1) {
		this.politicalArea1 = politicalArea1;
	}
	public Long getPoliticalArea2() {
		return politicalArea2;
	}
	public void setPoliticalArea2(Long politicalArea2) {
		this.politicalArea2 = politicalArea2;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getPoliticalArea1Name() {
		return politicalArea1Name;
	}
	public void setPoliticalArea1Name(String politicalArea1Name) {
		this.politicalArea1Name = politicalArea1Name;
	}
	public String getPoliticalArea2Name() {
		return politicalArea2Name;
	}
	public void setPoliticalArea2Name(String politicalArea2Name) {
		this.politicalArea2Name = politicalArea2Name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
