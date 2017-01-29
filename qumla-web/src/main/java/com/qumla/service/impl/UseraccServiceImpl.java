package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.user.Session;
import com.qumla.domain.user.Useracc;
import com.qumla.web.controller.LoginController;
import com.qumla.web.controller.RequestWrapper;
@Component("useraccServiceImpl")
public class UseraccServiceImpl implements ResourceRepository<Useracc, Long>{
	@Autowired
	private UseraccDaoMapper userMapper;
	
	public UseraccServiceImpl() {
	}

	@Override
	public Useracc findOne(Long id, QueryParams requestParams) {
		Session session=RequestWrapper.getSession();
		if(session.getUseracc()!=null && session.getUseracc().getId().equals(id)){
			return userMapper.findOne(id);
		}else{
			return LoginController.createAnonymeUser();
		}
	}

	@Override
	public Iterable<Useracc> findAll(QueryParams requestParams) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Useracc> findAll(Iterable<Long> ids,
			QueryParams requestParams) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Useracc> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

}
