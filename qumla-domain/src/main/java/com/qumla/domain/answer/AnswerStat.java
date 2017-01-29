package com.qumla.domain.answer;

import io.katharsis.resource.annotations.JsonApiIncludeByDefault;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToOne;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;

import com.qumla.domain.AbstractEntity;
import com.qumla.domain.location.Country;
import com.qumla.domain.location.Location;
import com.qumla.domain.location.LocationData;
import com.qumla.domain.question.Option;
import com.qumla.domain.question.Question;

@JsonApiResource(type="answerstat")
public class AnswerStat extends AbstractEntity{

	private static final long serialVersionUID = -2119864216618794191L;
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
	private LocalDate answerdate;
	private int hour;
	private long total;
	private float percent;
	private int indexed;
	
	public AnswerStat() {
		// TODO Auto-generated constructor stub
	}

	public boolean isSerialVersionUID(){
		return true;
	}
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
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
		Calendar cal=Calendar.getInstance();
		if(a.getCreateDt()!=null){
			cal.setTime(a.getCreateDt());
		}
		this.setHour(cal.get(Calendar.HOUR_OF_DAY));
		this.setAnswerdate(Instant.ofEpochMilli(a.getCreateDt().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
	}



	public int getHour() {
		return hour;
	}


	public void setHour(int hour) {
		this.hour = hour;
	}


	public LocalDate getAnswerdate() {
		return answerdate;
	}


	public void setAnswerdate(LocalDate answerdate) {
		this.answerdate = answerdate;
	}



	public float getPercent() {
		return percent;
	}


	public void setPercent(float percent) {
		this.percent = percent;
	}


	public long getTotal() {
		return total;
	}


	public void setTotal(long total) {
		this.total = total;
	}

	public int getIndexed() {
		return indexed;
	}

	public void setIndexed(int indexed) {
		this.indexed = indexed;
	}


}
