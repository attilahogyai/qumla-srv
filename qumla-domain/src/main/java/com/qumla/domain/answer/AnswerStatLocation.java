package com.qumla.domain.answer;

import io.katharsis.resource.annotations.JsonApiIncludeByDefault;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToOne;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.qumla.domain.AbstractEntity;
import com.qumla.domain.location.Country;
import com.qumla.domain.location.Location;
import com.qumla.domain.location.LocationData;
import com.qumla.domain.question.Option;
import com.qumla.domain.question.Question;
@JsonApiResource(type="answerstatlocation")
public class AnswerStatLocation extends AbstractEntity{

	private static final long serialVersionUID = 3443442709351349082L;

	private Question question;
	@JsonApiIncludeByDefault	
	@JsonApiToOne
	private Option option;
	@JsonApiIncludeByDefault
	@JsonApiToOne
	private LocationData location;
	@JsonApiToOne
	@JsonApiIncludeByDefault
	private Country country;
	private long count;
	private long total;
	private float percent;
	private Option top;
	private LocalDate firstdate;
	public AnswerStatLocation() {
		// TODO Auto-generated constructor stub
	}
	public boolean isSerialVersionUID(){
		return true;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public Option getOption() {
		return option;
	}
	public void setOption(Option option) {
		this.option = option;
	}
	public LocationData getLocation() {
		return location;
	}
	public void setLocation(LocationData location) {
		this.location = location;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public float getPercent() {
		return percent;
	}
	public void setPercent(float percent) {
		this.percent = percent;
	}
	public void copyFromAnswer(Answer a){
		Question q=new Question(); 
		q.setId(a.getQuestion());
		this.setQuestion(q);
		//this.setAnswerDate(a.getCreateDt());
		Country c=new Country();
		c.setCode(a.getCountry());
		this.setCountry(c);
		LocationData l=new LocationData();
		l.setId(a.getLocation());
		this.setLocation(l);
		Option o=new Option(); 
		o.setId(a.getOption());
		this.setOption(o);
		this.setFirstdate(Instant.ofEpochMilli(a.getCreateDt().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
	}
	public LocalDate getFirstdate() {
		return firstdate;
	}
	public void setFirstdate(LocalDate firstdate) {
		this.firstdate = firstdate;
	}
	public Option getTop() {
		return top;
	}
	public void setTop(Option top) {
		this.top = top;
	}

}
