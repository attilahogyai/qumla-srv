package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.MetaRepository;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.response.MetaInformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.user.Session;
import com.qumla.domain.user.Subscription;
import com.qumla.web.controller.RequestWrapper;
@Component("subscriptionServiceImpl")
public class SubscriptionServiceImpl extends AbstractService implements ResourceRepository<Subscription, Long>, MetaRepository<Subscription>{
	@Autowired
	private SubscriptionDaoMapper subsDaoMapper;
	
	@Override
	public MetaInformation getMetaInformation(Iterable<Subscription> resources, QueryParams queryParams) {
		return null;
	}

	@Override
	public Subscription findOne(Long id, QueryParams queryParams) {
		return subsDaoMapper.findOne(id);
	}

	@Override
	public Iterable<Subscription> findAll(QueryParams queryParams) {
		return null;
	}

	@Override
	public Iterable<Subscription> findAll(Iterable<Long> ids, QueryParams queryParams) {
		return null;
	}

	@Override
	public <S extends Subscription> S save(S entity) {
		Session session=RequestWrapper.getSession();
		entity.setSession(session);
		if(entity.getId()==null){
			subsDaoMapper.insert(entity);
		}else{
			subsDaoMapper.update(entity);
		}
		return entity;
	}

	@Override
	public void delete(Long id) {
		subsDaoMapper.delete(id);
		
	}

	public SubscriptionDaoMapper getSubsDaoMapper() {
		return subsDaoMapper;
	}

	public void setSubsDaoMapper(SubscriptionDaoMapper subsDaoMapper) {
		this.subsDaoMapper = subsDaoMapper;
	}

}
