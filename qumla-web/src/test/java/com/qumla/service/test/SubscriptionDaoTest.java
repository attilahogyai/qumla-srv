package com.qumla.service.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.qumla.domain.PagingFilter;
import com.qumla.domain.question.Question;
import com.qumla.domain.question.QuestionFilter;
import com.qumla.domain.user.Subscription;
import com.qumla.service.impl.LoginDaoMapper;
import com.qumla.service.impl.QuestionDaoMapper;
import com.qumla.service.impl.SubscriptionDaoMapper;
import com.qumla.service.impl.SubscriptionServiceImpl;
import com.qumla.web.controller.RequestWrapper;

public class SubscriptionDaoTest extends AbstractTest {
	@Before
	public void before() {
		super.before(LoginDaoMapper.class);
		
	}
	@Test
	public void subscriptionTest() throws Exception {

		SubscriptionDaoMapper mapper=(SubscriptionDaoMapper)getMapper(SubscriptionDaoMapper.class);
		SubscriptionServiceImpl ss=new SubscriptionServiceImpl();
		ss.setSubsDaoMapper(mapper);
		
		Subscription s=new Subscription();
		s.setQuestion(910);
		s.setEmail("attila.hogyai@gmail.com");
		s.setUseracc(1970);
		s.setSession(RequestWrapper.getSession());
		ss.save(s);
		
		
		
		QuestionDaoMapper qmapper=(QuestionDaoMapper)getMapper(QuestionDaoMapper.class);
		QuestionFilter pf=new QuestionFilter("HU");
		pf.setId(910L);
		pf.setSession(RequestWrapper.getSession().getCode());
		Question q=qmapper.findOneQuestion(pf);
		
		Assert.assertTrue(q.isSubscribed());
	}
}
