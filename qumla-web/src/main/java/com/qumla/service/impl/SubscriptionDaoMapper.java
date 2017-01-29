package com.qumla.service.impl;

import com.qumla.domain.user.Subscription;

public interface SubscriptionDaoMapper {
	public Subscription findOne(Long id);
	public void insert(Subscription location);
	public void update(Subscription location);
	public void delete(Long id);

}
