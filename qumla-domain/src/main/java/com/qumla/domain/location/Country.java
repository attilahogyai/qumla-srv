package com.qumla.domain.location;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;

@JsonApiResource(type="country")
public class Country {
	private String countryName;
	@JsonApiId
	private String code;
	
	private long questionCount;
	
	private String defLang;
	
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	@Override
	public boolean equals(Object arg0) {
		if(arg0==null) return false;
		if(arg0 instanceof Country){
			return this.getCode().equals(((Country) arg0).code);
		}
		return false;
	}
	@Override
	public int hashCode() {
		return this.getCode().toString().length();
	}

	public long getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(long questionCount) {
		this.questionCount = questionCount;
	}

	public String getDefLang() {
		return defLang;
	}

	public void setDefLang(String defLang) {
		this.defLang = defLang;
	}


}
