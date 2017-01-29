package com.qumla.service.impl;

import java.util.List;

import com.qumla.domain.comment.Comment;

public interface CommentDaoMapper {
	public void insertComment(Comment comment);
	public List<Comment> findForQuestion(Long qid);
	public Comment findOne(Long qid);
	public List<Comment> findRecent();
	
	public void updateComment(Comment comment);
	
}
