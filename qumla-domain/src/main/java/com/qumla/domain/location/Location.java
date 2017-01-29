package com.qumla.domain.location;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;

import com.qumla.domain.AbstractEntity;
import com.qumla.domain.question.Question;
@JsonApiResource(type="location")
public class Location extends AbstractEntity {
	@JsonApiId
	private Long id=0L;
	private String cityName;
	private String stateCode;
	private String countryCode;
	private Float lon;
	private Float lat;
	private String zipCode;
	private String timezoneId;
	public Location() {
		// TODO Auto-generated constructor stub
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public boolean equals(Object arg0) {
		if(arg0==null) return false;
		if(arg0 instanceof Location){
			return this.getId().equals(((Location) arg0).id);
		}
		return false;
	}
	@Override
	public int hashCode() {
		if(this.getId()!=null){
			return this.getId().toString().length();
		}else{
			return 0;
		}
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public Float getLon() {
		return lon;
	}
	public void setLon(Float lon) {
		this.lon = lon;
	}

	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public Float getLat() {
		return lat;
	}
	public void setLat(Float lat) {
		this.lat = lat;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getTimezoneId() {
		return timezoneId;
	}
	public void setTimezoneId(String timezoneId) {
		this.timezoneId = timezoneId;
	}
}
