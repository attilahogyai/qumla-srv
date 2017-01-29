package com.qumla.domain;

import io.katharsis.response.MetaInformation;

public class Metadata implements MetaInformation{
	private Long count;
	private Long totalPages;
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}
	
}
