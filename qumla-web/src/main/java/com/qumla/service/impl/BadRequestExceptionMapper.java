package com.qumla.service.impl;

import io.katharsis.errorhandling.ErrorData;
import io.katharsis.errorhandling.ErrorResponse;
import io.katharsis.errorhandling.mapper.ExceptionMapperProvider;
import io.katharsis.errorhandling.mapper.JsonApiExceptionMapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.qumla.web.exception.BadRequest;

@ExceptionMapperProvider
public class BadRequestExceptionMapper implements JsonApiExceptionMapper<BadRequest>{

	@Override
	public ErrorResponse toErrorResponse(BadRequest exception) {
		List l= new ArrayList();
		l.add(new ErrorData(null,null,"error",exception.getMessage(),null,null,null,null,null));
		return new ErrorResponse(l,HttpStatus.BAD_REQUEST.value());
	}

}
