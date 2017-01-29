package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.comment.Comment;
import com.qumla.domain.question.Question;
@Component("questionToCommentRelation")
public class QuestionToCommentRelation implements RelationshipRepository<Comment, Long, Question, Long>{
	@Autowired
	private CommentDaoMapper commentMapper;
	@Override
	public void setRelation(Comment source, Long targetId, String fieldName) {
		Question q=new Question();
		q.setId(targetId);
		source.setQuestion(q);
		commentMapper.updateComment(source);
		
	}

	@Override
	public void setRelations(Comment source, Iterable<Long> targetIds,
			String fieldName) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
		
	}

	@Override
	public void addRelations(Comment source, Iterable<Long> targetIds,
			String fieldName) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
		
	}

	@Override
	public void removeRelations(Comment source, Iterable<Long> targetIds,
			String fieldName) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
		
	}

	@Override
	public Question findOneTarget(Long sourceId, String fieldName,
			QueryParams requestParams) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Question> findManyTargets(Long sourceId, String fieldName,
			QueryParams requestParams) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
