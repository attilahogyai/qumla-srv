package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.RelationshipRepository;

import org.springframework.stereotype.Component;

import com.qumla.domain.comment.Comment;
@Component(value="commentToCommentRelation")
public class CommentToCommentRelation implements RelationshipRepository<Comment, Long, Comment, Long>{

	@Override
	public void setRelation(Comment source, Long targetId, String fieldName) {
		Comment c=new Comment();
		c.setId(targetId);
		source.setOriginal(c);
	}

	@Override
	public void setRelations(Comment source, Iterable<Long> targetIds,
			String fieldName) {
		throw new UnsupportedOperationException();
		
		
	}

	@Override
	public void addRelations(Comment source, Iterable<Long> targetIds,
			String fieldName) {
		throw new UnsupportedOperationException();
		
		
	}

	@Override
	public void removeRelations(Comment source, Iterable<Long> targetIds,
			String fieldName) {
		throw new UnsupportedOperationException();
		
		
	}

	@Override
	public Iterable<Comment> findManyTargets(Long arg0, String arg1,
			QueryParams arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Comment findOneTarget(Long arg0, String arg1, QueryParams arg2) {
		throw new UnsupportedOperationException();
	}


}
