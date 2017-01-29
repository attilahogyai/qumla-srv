package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.answer.AnswerStatLocation;
@Component(value="answerStatLocationServiceImpl")
public class AnswerStatLocationServiceImpl extends AbstractService implements ResourceRepository<AnswerStatLocation, Long> {
	@Autowired
	private AnswerDaoMapper answerServiceMapper;
	@Override
	public AnswerStatLocation findOne(Long id, QueryParams requestParams) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<AnswerStatLocation> findAll(QueryParams requestParams) {
		long qid = getQuestionId(requestParams);
		return findByQuestion(qid);
	}
	public List<AnswerStatLocation> findByQuestion(Long questionId) {
		List<AnswerStatLocation> as=answerServiceMapper.findAnswerStatLocationForQuestion(questionId);
		Map <Long,Long> topOptionByCity=new HashMap<Long,Long>();
		long sum=0;
		for (AnswerStatLocation answerStat : as) {
			sum+=answerStat.getCount();
		}
		for (AnswerStatLocation answerStat : as) {
			answerStat.setPercent(((float)answerStat.getCount())/sum*100);
			answerStat.setTotal(sum);
		}
		
		return as;
	}

	@Override
	public Iterable<AnswerStatLocation> findAll(Iterable<Long> ids,
			QueryParams requestParams) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends AnswerStatLocation> S save(S entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Long id) {
		throw new UnsupportedOperationException();
	}

}
