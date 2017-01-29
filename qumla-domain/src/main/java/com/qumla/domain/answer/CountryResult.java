package com.qumla.domain.answer;

public class CountryResult {
	private String country;
	private Long count;
	private Long max;
	private String countryname;
	private Long option;
	private String text;
	private Float percent;
	private Long total;
	private String region;
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
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
	public String getCountryname() {
		return countryname;
	}
	public void setCountryname(String countryname) {
		this.countryname = countryname;
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
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Float getPercent() {
		return percent;
	}
	public void setPercent(Float percent) {
		this.percent = percent;
	}
	@Override
	public Object clone() {
		CountryResult r=new CountryResult();
		r.countryname=this.countryname;
		r.count=this.count;
		r.country=this.country;
		r.max=this.max;
		r.option=this.option;
		r.total=this.total;
		r.region=this.region;
		r.text=this.text;
		return r;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}	
}
