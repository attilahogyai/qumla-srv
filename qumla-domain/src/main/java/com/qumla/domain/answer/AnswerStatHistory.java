package com.qumla.domain.answer;

import java.util.Date;

public class AnswerStatHistory {
	private Long option;
	private Date date;
	private Long count;
	private Integer percent;
	public Long getOption() {
		return option;
	}
	public void setOption(Long option) {
		this.option = option;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Integer getPercent() {
		return percent;
	}
	public void setPercent(Integer percent) {
		this.percent = percent;
	}
	
}
