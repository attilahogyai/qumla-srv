package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;
import io.katharsis.repository.ResourceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.comment.Comment;
import com.qumla.domain.user.Session;
import com.qumla.util.BBCodeParser;
import com.qumla.web.controller.RequestWrapper;

@Component(value="commentServiceImpl")
public class CommentServiceImpl extends AbstractService implements ResourceRepository<Comment, Long>,
RelationshipRepository<Comment, Long, Session, String>{
	@Autowired
	private CommentDaoMapper commentMapper;
	@Override
	public Comment findOne(Long id, QueryParams requestParams) {
		return commentMapper.findOne(id);
	}

	@Override
	public Iterable<Comment> findAll(QueryParams requestParams) {
		if(requestParams.getFilters().getParams().get("question")!=null){
			long qid = getQuestionId(requestParams);
			return commentMapper.findForQuestion(qid);
		}else{
			return commentMapper.findRecent();			
		}
		
	}

	@Override
	public Iterable<Comment> findAll(Iterable<Long> ids,
			QueryParams requestParams) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends Comment> S save(S entity) {
		Session s=new Session();
		s.setCode(RequestWrapper.getSession().getCode()); // the session code should be copied from active session  
		entity.setSession(s.getCode());	
		entity.setStatus(1);
		entity.setComment(BBCodeParser.parseString(entity.getComment()));
		commentMapper.insertComment(entity);
		return entity;
	}

	@Override
	public void delete(Long id) {
		throw new UnsupportedOperationException();
		
		
	}

	@Override
	public void setRelation(Comment source, String targetId, String fieldName) {
		Session s=new Session();
		s.setCode(RequestWrapper.getSession().getCode()); // the session code should be copied from active session  
		source.setSession(s.getCode());
		commentMapper.updateComment(source);
	}

	@Override
	public void setRelations(Comment source, Iterable<String> targetIds,
			String fieldName) {
		throw new UnsupportedOperationException();
		
		
	}

	@Override
	public void addRelations(Comment source, Iterable<String> targetIds,
			String fieldName) {
		throw new UnsupportedOperationException();
		
		
	}

	@Override
	public void removeRelations(Comment source, Iterable<String> targetIds,
			String fieldName) {
		throw new UnsupportedOperationException();
		
		
	}

	@Override
	public Session findOneTarget(Long sourceId, String fieldName,
			QueryParams requestParams) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Session> findManyTargets(Long sourceId, String fieldName,
			QueryParams requestParams) {
		throw new UnsupportedOperationException();
	}

}
