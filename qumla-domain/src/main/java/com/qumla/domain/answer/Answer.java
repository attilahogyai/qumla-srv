package com.qumla.domain.answer;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;

import java.util.Date;

import com.qumla.domain.AbstractEntity;

@JsonApiResource(type="answer")
public class Answer extends AbstractEntity{
	private static final long serialVersionUID = -4018971391481228567L;

	private Long question;
	private Long option;
	private String session;
	private Long location;
	private String country;
	private Date createDt;
	
	public Answer() {
		// TODO Auto-generated constructor stub
	}
	public boolean isSerialVersionUID(){
		return true;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getSession() {
		return session;
	}


	public void setSession(String session) {
		this.session = session;
	}


	public Long getLocation() {
		return location;
	}


	public void setLocation(Long location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public Long getQuestion() {
		return question;
	}


	public void setQuestion(Long question) {
		this.question = question;
	}


	public Long getOption() {
		return option;
	}


	public void setOption(Long option) {
		this.option = option;
	}


}
