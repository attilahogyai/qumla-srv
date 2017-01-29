package com.qumla.domain.user;

import com.qumla.domain.AbstractRp;

public class UserLoginRp extends AbstractRp{
	private String responseCode;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
}
