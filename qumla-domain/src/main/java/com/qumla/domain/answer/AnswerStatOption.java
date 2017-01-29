package com.qumla.domain.answer;

import io.katharsis.resource.annotations.JsonApiIncludeByDefault;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToOne;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.qumla.domain.AbstractEntity;
import com.qumla.domain.question.Option;
import com.qumla.domain.question.Question;
@JsonApiResource(type="answerstatoption")
public class AnswerStatOption  extends AbstractEntity{

	private static final long serialVersionUID = 2125691842080136564L;

	private Question question;
	@JsonApiIncludeByDefault	
	@JsonApiToOne
	private Option option;
	private long count;
	private long total;
	private float percent;
	private LocalDate firstdate;
	public boolean isSerialVersionUID(){
		return true;
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

}
