package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.answer.AnswerStat;

@Component(value="answerStatServiceImpl")

public class AnswerStatServiceImpl extends AbstractService implements ResourceRepository<AnswerStat, Long> {
	final Logger logger = LoggerFactory.getLogger(AnswerStatServiceImpl.class);

	@Autowired
	private AnswerDaoMapper answerServiceMapper;

	@Override
	public AnswerStat findOne(Long id, QueryParams requestParams) {
		return answerServiceMapper.findOneAnswerStat(id);
	}
	@Override
	public Iterable<AnswerStat> findAll(QueryParams requestParams) {
		long qid = getQuestionId(requestParams);
		return findByQuestion(qid);
	}
	public List<AnswerStat> findByQuestion(Long questionId) {
		List<AnswerStat> as=answerServiceMapper.findAnswerStatForQuestion(questionId);
		long sum=0;
		for (AnswerStat answerStat : as) {
			sum+=answerStat.getCount();
		}
		for (AnswerStat answerStat : as) {
			answerStat.setTotal(sum);
		}
		
		return as;
	}
	public long getAnswerStatCountForQuestion(Long qid){
		return answerServiceMapper.getAnswerStatCountForQuestion(qid);
	}


	
	@Override
	public Iterable<AnswerStat> findAll(Iterable<Long> ids,
			QueryParams requestParams) {
		return null;
	}

	@Override
	public <S extends AnswerStat> S save(S entity) {
		if(entity.getId()!=null){
			try {
				answerServiceMapper.saveAnswerStat(entity);
			} catch (SQLException e) {
				logger.error("error saving Answer");
			}
		}else{
			answerServiceMapper.updateAnswerStatById(entity);
		}
		return entity;
	}

	@Override
	public void delete(Long id) {
		answerServiceMapper.deleteAnswerStat(id);
	}

	public AnswerDaoMapper getAnswerServiceMapper() {
		return answerServiceMapper;
	}

	public void setAnswerServiceMapper(AnswerDaoMapper answerServiceMapper) {
		this.answerServiceMapper = answerServiceMapper;
	}


}
