package com.qumla.domain.dictionary;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;

import com.qumla.domain.AbstractEntity;
@JsonApiResource(type="langtext")
public class Langtext extends AbstractEntity{
	@JsonApiId
	private Long id;	
	
	private String code;
	private String type;
	public boolean isSerialVersionUID(){
		return true;
	}
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getServer() {
		return server;
	}
	public void setServer(int server) {
		this.server = server;
	}
	private String language;
	private String text;
	private int server;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
