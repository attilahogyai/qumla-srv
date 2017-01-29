package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.answer.Answer;
import com.qumla.domain.answer.AnswerStat;
import com.qumla.domain.answer.AnswerStatLocation;
import com.qumla.domain.answer.AnswerStatOption;
import com.qumla.domain.question.Question;
import com.qumla.domain.user.Session;
import com.qumla.service.AnswerService;
import com.qumla.web.controller.RequestWrapper;
import com.qumla.web.exception.BadRequest;

@Component("answerServiceImpl")
public class AnswerServiceImpl extends AbstractService implements AnswerService, ResourceRepository<Answer, Long> {
	@Autowired
	private AnswerDaoMapper answerServiceMapper;
	@Autowired
	private QuestionServiceImpl questionServiceImpl;
	@Override
	public Answer findOne(Long id, QueryParams requestParams) {
		return answerServiceMapper.findOneAnswer(id);
	}

	@Override
	public Iterable<Answer> findAll(QueryParams requestParams) {
		return null;
	}

	@Override
	public Iterable<Answer> findAll(Iterable<Long> ids,
			QueryParams requestParams) {
		return null;
	}

	@Override
	public synchronized <S extends Answer> S save(S entity){
		if(entity.getCreateDt()==null){
			entity.setCreateDt(new Date());
		}
		Session session=RequestWrapper.getSession();
		entity.setSession(session.getCode());
		entity.setLocation(session.getLocation());
		entity.setCountry(session.getCountry());
		List<Answer> a=answerServiceMapper.findOneByQuestionAndSession(entity.getSession(),entity.getQuestion());
		if(a!=null && a.size()>0){
			Question q=questionServiceImpl.findOne(a.get(0).getQuestion(), null);
			if(!q.isMultiple()){
				throw new BadRequest("QUESTION_MULTIPLE_VOTE_TRIES");
			}
			if(q.isClosed()){
				throw new BadRequest("QUESTION_IS_CLOSED");
			}			
		}
		
		answerServiceMapper.insertAnswer(entity);
		questionServiceImpl.incrementAnswerCount(entity.getQuestion());
		updateAnswerStat(entity);
		return entity;
	}

	public void updateAnswerStat(Answer entity) {
		AnswerStat as=new AnswerStat();
		as.copyFromAnswer(entity);
		int result=answerServiceMapper.incrementAnswerStatByFields(as);
		if(result==0){
			try{
				answerServiceMapper.saveAnswerStat(as);
			}catch(SQLException e){
				result=answerServiceMapper.incrementAnswerStatByFields(as);
				if(result==0){
					logger.error("unable to increment count for:"+as.getOption().getId()+
							" "+as.getLocation().getId()+
							" "+as.getCountry().getCode()+
							" "+as.getAnswerdate()+
							" "+as.getHour());
				}
			}
		}else if(result>1){
			logger.error("FATAL ERROR more than one row for answer_stat:"+as.getOption().getId()+
					" "+as.getLocation().getId()+
					" "+as.getCountry().getCode()+
					" "+as.getAnswerdate()+
					" "+as.getHour());
		}
		updateAnswerStatLocation(entity);
		updateAnswerStatOption(entity);
		answerServiceMapper.setAnswerIndexed(entity);
	}
	private void updateAnswerStatLocation(Answer entity) {
		AnswerStatLocation asl=new AnswerStatLocation();
		asl.copyFromAnswer(entity);
		int result=answerServiceMapper.incrementAnswerStatLocationByFields(asl);
		if(result==0){
			try{
				answerServiceMapper.saveAnswerStatLocation(asl);
			}catch(SQLException e){
				result=answerServiceMapper.incrementAnswerStatLocationByFields(asl);
				if(result==0){
					logger.error("unable to increment count for:"+asl.getOption().getId()+
							" "+asl.getLocation().getId()+
							" "+asl.getCountry().getCode());
				}
			}
		}else if(result>1){
			logger.error("FATAL ERROR more than one row for answer_stat_location:"+asl.getOption().getId()+
					" "+asl.getLocation().getId()+
					" "+asl.getCountry().getCode());
		}
	}	
	private void updateAnswerStatOption(Answer entity) {
		AnswerStatOption asl=new AnswerStatOption();
		asl.copyFromAnswer(entity);
		int result=answerServiceMapper.incrementAnswerStatOptionByFields(asl);
		if(result==0){
			try{
				answerServiceMapper.saveAnswerStatOption(asl);
			}catch(SQLException e){
				result=answerServiceMapper.incrementAnswerStatOptionByFields(asl);
				if(result==0){
					logger.error("unable to increment count for:"+asl.getOption().getId());
				}
			}
		}else if(result>1){
			logger.error("FATAL ERROR more than one row for answer_stat_location:"+asl.getOption().getId());
		}
	}		
	public List<Answer> findAllAnswer(Iterable<Long> ids,
			QueryParams requestParams){
		return null;
	}
	public List<Answer> findAnswerForQuestion(@Param("qid") Long qid){
		return answerServiceMapper.findAnswerForQuestion(qid);
	}

	@Override
	public void delete(Long id) {
		answerServiceMapper.deleteAnswer(id);
	}

	public AnswerDaoMapper getAnswerServiceMapper() {
		return answerServiceMapper;
	}

	public void setAnswerServiceMapper(AnswerDaoMapper answerServiceMapper) {
		this.answerServiceMapper = answerServiceMapper;
	}

	public QuestionServiceImpl getQuestionServiceImpl() {
		return questionServiceImpl;
	}

	public void setQuestionServiceImpl(QuestionServiceImpl questionServiceImpl) {
		this.questionServiceImpl = questionServiceImpl;
	}

	@Override
	public void resetStatForQuestion(Long qid) {
		answerServiceMapper.updateLocationForAnswers(qid);
		answerServiceMapper.resetAnswerStatForQuestion(qid);
		answerServiceMapper.resetAnswerStatLocationForQuestion(qid);
		answerServiceMapper.resetAnswerStatOptionForQuestion(qid);
		answerServiceMapper.resetIndexedAnswersForQuestion(qid);
	}




}
