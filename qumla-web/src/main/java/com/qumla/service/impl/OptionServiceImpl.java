package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.repository.ResourceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.PagingFilter;
import com.qumla.domain.question.Option;
import com.qumla.domain.question.Question;
import com.qumla.domain.question.QuestionFilter;
import com.qumla.web.controller.RequestWrapper;
import com.qumla.web.exception.AccessDenied;
@Component("optionServiceImpl")
public class OptionServiceImpl extends AbstractService implements ResourceRepository<Option, Long>,
	RelationshipRepository<Option, Long, Question, Long>{
	final Logger logger = LoggerFactory.getLogger(OptionServiceImpl.class);

	@Autowired
	private QuestionDaoMapper questionServiceMapper;

	@Override
	public Option findOne(Long id, QueryParams requestParams) {
		return questionServiceMapper.findOneOption(id);
	}

	@Override
	public Iterable<Option> findAll(QueryParams requestParams) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Iterable<Option> findAll(Iterable<Long> ids,
			QueryParams requestParams) {
		throw new UnsupportedOperationException();

	}

	@Override
	public <S extends Option> S save(S entity) {
		
		PagingFilter qf=new QuestionFilter(RequestWrapper.getSession().getCountry());
		qf.setId(entity.getQuestion().getId());
		Question q=questionServiceMapper.findOneQuestion(qf);
		
		if(q.getSessionCode().equals(RequestWrapper.getSession().getCode()) || RequestWrapper.getSession().isAdmin()){
			if(entity.getId()!=null){
				questionServiceMapper.updateOption(entity);
			}else{
				questionServiceMapper.insertOption(entity);
			}	
		}else{
			throw new AccessDenied();
		}

		return entity;
	}

	@Override
	public void delete(Long id) {
		Option o=this.findOne(id, null);
		PagingFilter qf=new QuestionFilter(RequestWrapper.getSession().getCountry());
		qf.setId(o.getQuestion().getId());
		Question q=questionServiceMapper.findOneQuestion(qf);

		if(q.getSessionCode().equals(RequestWrapper.getSession().getCode())){
			o.setId(id);
			questionServiceMapper.deleteOption(o);
		}else{
			throw new AccessDenied();
		}
	}
	@Override
	public void setRelation(Option source, Long targetId, String fieldName) {
		logger.debug("setRelation source:"+source+" targetId:"+targetId+" fieldName:"+fieldName);
		Question q=new Question();
		q.setId(targetId);
		source.setQuestion(q);
		questionServiceMapper.updateOption(source);
	}

	@Override
	public void setRelations(Option source, Iterable<Long> targetIds,
			String fieldName) {
		logger.debug("setRelations source:"+source+" targetId:"+targetIds+" fieldName:"+fieldName);
	}

	@Override
	public void addRelations(Option source, Iterable<Long> targetIds,
			String fieldName) {
		logger.debug("addRelations source:"+source+" targetId:"+targetIds+" fieldName:"+fieldName);
		questionServiceMapper.updateOption(source);
	}

	@Override
	public void removeRelations(Option source, Iterable<Long> targetIds,
			String fieldName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Question findOneTarget(Long sourceId, String fieldName,
			QueryParams requestParams) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Question> findManyTargets(Long sourceId, String fieldName,
			QueryParams requestParams) {
		throw new UnsupportedOperationException();

	}
}
