package com.qumla.domain.question;

import com.qumla.domain.PagingFilter;

public class QuestionFilter extends PagingFilter{
	String session;
	boolean sessionFilter;
	boolean answered;
	boolean latest;
	String tags[];
	String tag;
	
	public QuestionFilter(String country){
		super(country);
	}
	
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public boolean isAnswered() {
		return answered;
	}
	public void setAnswered(boolean answered) {
		this.answered = answered;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public boolean isSessionFilter() {
		return sessionFilter;
	}
	public void setSessionFilter(boolean sessionFilter) {
		this.sessionFilter = sessionFilter;
	}
	public boolean isLatest() {
		return latest;
	}
	public void setLatest(boolean latest) {
		this.latest = latest;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
}
