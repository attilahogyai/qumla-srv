package com.qumla.domain.question;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qumla.domain.AbstractEntity;
@JsonApiResource(type="option")
public class Option extends AbstractEntity{
	@JsonIgnore
	private static final long serialVersionUID = 4098001501173897604L;

    @JsonApiToOne
	private Question question;
	private String text;
	private String style;
	private int ord;
	public boolean isSerialVersionUID(){
		return true;
	}
	public Option() {
		// TODO Auto-generated constructor stub
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	@Override
	public boolean equals(Object arg0) {
		if(arg0==null) return false;
		if(arg0 instanceof Option){
			return this.getId().equals(((Option) arg0).id);
		}
		return false;
	}
	@Override
	public int hashCode() {
		return this.getId().toString().length();
	}
	public int getOrd() {
		return ord;
	}
	public void setOrd(int order) {
		this.ord = order;
	}

}
