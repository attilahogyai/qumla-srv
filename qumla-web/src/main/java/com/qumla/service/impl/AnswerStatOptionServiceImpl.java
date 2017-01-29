package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.answer.AnswerStatOption;
import com.qumla.domain.question.Question;
@Component(value="answerStatOptionServiceImpl")

public class AnswerStatOptionServiceImpl extends AbstractService implements ResourceRepository<AnswerStatOption, Long> {
	@Autowired
	private AnswerDaoMapper answerServiceMapper;
	@Override
	public AnswerStatOption findOne(Long id, QueryParams requestParams) {
		throw new UnsupportedOperationException();
	}
	@Override
	public Iterable<AnswerStatOption> findAll(QueryParams requestParams) {
		long qid=getQuestionId(requestParams);
		return findByQuestion(qid);
	}
	public List<AnswerStatOption> findByQuestion(Long questionId) {
		List<AnswerStatOption> as=answerServiceMapper.findAnswerStatOptionForQuestion(questionId);
		long sum=0;
		Question q=new Question();
		q.setId(questionId);
		for (AnswerStatOption answerStat : as) {
			sum+=answerStat.getCount();
			answerStat.getOption().setQuestion(q);
		}
		for (AnswerStatOption answerStat : as) {
			answerStat.setTotal(sum);
		}
		
		return as;
	}
	@Override
	public Iterable<AnswerStatOption> findAll(Iterable<Long> ids,
			QueryParams requestParams) {
		throw new UnsupportedOperationException();
	}	
	@Override
	public <S extends AnswerStatOption> S save(S entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Long id) {
		throw new UnsupportedOperationException();
		
		
	}

}
