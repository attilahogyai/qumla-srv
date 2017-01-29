package com.qumla.domain.answer;

public class LocationResult {
	private Long location;
	private Long count;
	private Long max;
	private String name;
	private String region;
	private Long option;
	private String text;
	private Float percent;
	private Long total;
	private Float lon;
	private Float lat;
	public Long getLocation() {
		return location;
	}
	public void setLocation(Long location) {
		this.location = location;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Long getMax() {
		return max;
	}
	public void setMax(Long max) {
		this.max = max;
	}
	
	public Long getOption() {
		return option;
	}
	public void setOption(Long option) {
		this.option = option;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Float getPercent() {
		return percent;
	}
	public void setPercent(Float percent) {
		this.percent = percent;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	@Override
	public Object clone() {
		LocationResult r=new LocationResult();
		r.name=this.name;
		r.count=this.count;
		r.location=this.location;
		r.max=this.max;
		r.option=this.option;
		r.total=this.total;
		r.text=this.text;
		return r;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
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
	
}
