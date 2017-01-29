package com.qumla.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import io.katharsis.queryParams.QueryParams;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qumla.domain.answer.Answer;
import com.qumla.domain.answer.AnswerStat;
import com.qumla.domain.answer.AnswerStatLocation;
import com.qumla.domain.answer.AnswerStatOption;
import com.qumla.domain.answer.LocationResult;
import com.qumla.domain.location.Country;
import com.qumla.domain.location.Location;
import com.qumla.domain.location.LocationData;
import com.qumla.domain.question.Question;
import com.qumla.domain.question.QuestionFilter;
import com.qumla.domain.user.Session;
import com.qumla.service.impl.AnswerDaoMapper;
import com.qumla.service.impl.AnswerServiceImpl;
import com.qumla.service.impl.AnswerStatServiceImpl;
import com.qumla.service.impl.QuestionDaoMapper;
import com.qumla.service.impl.QuestionServiceImpl;
import com.qumla.web.controller.RequestWrapper;

public class AnswerDaoTest extends QuestionDaoTest {
	public void before() {
		super.before(AnswerDaoMapper.class);
	}
	
	@Test
	public void testAnswerInsert() throws Exception {
		
		AnswerServiceImpl asimpl=new AnswerServiceImpl();
		asimpl.setAnswerServiceMapper((AnswerDaoMapper)getMapper(AnswerDaoMapper.class));
		QuestionServiceImpl qsi=new QuestionServiceImpl();
		qsi.setQuestionServiceMapper((QuestionDaoMapper)getMapper(QuestionDaoMapper.class));
		asimpl.setQuestionServiceImpl(qsi);

		AnswerStatServiceImpl anwerSerImpl=new AnswerStatServiceImpl();
		anwerSerImpl.setAnswerServiceMapper((AnswerDaoMapper)getMapper(AnswerDaoMapper.class));
		
		QuestionServiceImpl questionService=createQuestionService();

		Question q=createQuestion(questionService);
		Answer a=new Answer();
		a.setCreateDt(new Date());
		
		Session s=new Session();
		s.setCode("testtoken");
		s.setLocation(11L);
		s.setCountry("HU");
		a.setSession(s.getCode());
		a.setOption(q.getOptions().get(0).getId());
		a.setQuestion(q.getId());
		RequestWrapper.setSession(s);
		asimpl.save(a);
		
		Answer a1=asimpl.findOne(a.getId(),new QueryParams());
		assertNotNull(a1);
		assertNotNull(a1.getOption());
		assertNotNull(a1.getCreateDt());
		
		a1.setCountry("HU");
		try{
			asimpl.save(a1);
		}catch(Exception e){
			assertNotNull(e);
		}
		ObjectMapper om=new ObjectMapper();
		List<AnswerStat> result=anwerSerImpl.findByQuestion(q.getId());
		assertNotNull(result);
		assertTrue(result.size()>0);
	}
	@Test
	public void testAnswerStatInsert() throws Exception {
		AnswerDaoMapper mapper=(AnswerDaoMapper)getMapper(AnswerDaoMapper.class);
		QuestionServiceImpl questionService=createQuestionService();
		Session s=new Session();
		s.setCode("testtoken");
		s.setCountry("HU");
		RequestWrapper.setSession(s);
		Question q=createQuestion(questionService);
		Country c=new Country();
		c.setCode("HU");
		AnswerStat a=new AnswerStat();
		a.setCountry(c);
		LocationData l = new LocationData();
		l.setId(new Long(11)); // Budapest
		a.setLocation(l);
		a.setAnswerdate(LocalDate.now());
		a.setHour((byte)10);
		a.setOption(q.getOptions().get(0));
		mapper.saveAnswerStat(a);
		
		AnswerStat a1=mapper.findOneAnswerStat(a.getId());
		assertNotNull(a1);
		assertNotNull(a1.getId());
		assertNotNull(a1.getOption().getId());
		assertNotNull(a1.getQuestion().getId());
		assertNotNull(a1.getLocation().getId());
		assertNotNull(a1.getCountry().getCode());
		assertNotNull(a1.getCountry().getCountryName());		
		assertNotNull(a1.getCount());
		assertNotNull(a1.getHour());
		assertNotNull(a1.getAnswerdate());
		
		a1.setCount(20);
		mapper.updateAnswerStatById(a1);
		AnswerStat a3=mapper.findOneAnswerStat(a1.getId());
		assertEquals(a3.getCount(), 20);
		
//		AnswerStat a4=mapper.findAnswerStatQuestion(a1.getQuestion().getId());
//		assertNotNull(a4);
//		assertEquals(a3.getQuestion().getId(),a4.getQuestion().getId());
//		assertEquals(10,a4.getHour());
	}
	@Test
	public void testAnswerStatOptionLocationFind() throws Exception {
		AnswerDaoMapper mapper=(AnswerDaoMapper)getMapper(AnswerDaoMapper.class);
		List<AnswerStatLocation> location=mapper.findAnswerStatLocationForQuestion(950L);
		Assert.isTrue(location.size()>0);
		Assert.isTrue(location.get(0).getCountry()!=null);
		Assert.isTrue(location.get(0).getLocation()!=null);
		Assert.isTrue(location.get(0).getCount()>0);
		Assert.isTrue(location.get(0).getOption()!=null);

		
		
		List<AnswerStatOption> options=mapper.findAnswerStatOptionForQuestion(950L);
		Assert.isTrue(options.size()>0);
		Assert.isTrue(options.get(0).getOption()!=null);
		
	}
	@Test
	public void testLocationResult() throws Exception {
		AnswerDaoMapper mapper=(AnswerDaoMapper)getMapper(AnswerDaoMapper.class);
		List<LocationResult> locationResult=(List<LocationResult>)mapper.locationResult(950L, "HU");
		Assert.isTrue(locationResult.size()>0);

		
	}
	public void createAnswerStat() throws Exception {
		beforeClass();
		before();
		QuestionDaoMapper question=(QuestionDaoMapper)getMapper(QuestionDaoMapper.class);
		AnswerDaoMapper mapper=(AnswerDaoMapper)getMapper(AnswerDaoMapper.class);			
		AnswerServiceImpl answerService=new AnswerServiceImpl();
		answerService.setAnswerServiceMapper(mapper);

		QuestionFilter filter=new QuestionFilter(RequestWrapper.getSession().getCountry());
		filter.setSession("aaaa");
		filter.setId(951L);
		filter.setOffset(0);
		filter.setLimit(100);
		List<Question> questions=question.findPopularQuestion(filter);
		for (Question question2 : questions) {
			answerService.resetStatForQuestion(question2.getId());
			List<Answer> answers= answerService.findAnswerForQuestion(question2.getId());
			for (Answer answer : answers) {
				if(answer.getLocation()!=null){
					answerService.updateAnswerStat(answer);
				}
			}
			for (SqlSession sqlsession : sessions) {
				sqlsession.commit();
			}
		}
	}
	
	
	public static final void main(String args[]) throws Exception{
		System.out.println();
		AnswerDaoTest t=new AnswerDaoTest();
		t.createAnswerStat();
	}
}
