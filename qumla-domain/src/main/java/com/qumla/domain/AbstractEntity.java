package com.qumla.domain;

import io.katharsis.resource.annotations.JsonApiId;


public class AbstractEntity extends AbstractQDI {
	@JsonApiId
	protected Long id;
	protected String sessionCode;
	private static final long serialVersionUID = 1L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSessionCode() {
		return sessionCode;
	}
	public void setSessionCode(String sessionCode) {
		this.sessionCode = sessionCode;
	}
	
}
