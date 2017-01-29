package com.qumla.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.qumla.domain.comment.Comment;
import com.qumla.domain.question.Question;
import com.qumla.service.impl.CommentDaoMapper;
import com.qumla.service.impl.QuestionDaoMapper;

public class CommentDaoTest extends QuestionDaoTest {
	@Test
	public void testComment() throws Exception {

		CommentDaoMapper mapper=(CommentDaoMapper)getMapper(CommentDaoMapper.class);
		
		Question q=createQuestion(createQuestionService());
		
		
		Comment c=new Comment();
		c.setComment("test comment");
		Timestamp t=new Timestamp(new Date().getTime());
		c.setCreatedt(t);
		c.setSession(q.getSession().getCode());
		c.setStatus(1);
		c.setQuestion(q);
		mapper.insertComment(c);
		
		Comment c2=new Comment();
		c2.setComment("child");
		Timestamp t2=new Timestamp(new Date().getTime());
		c2.setCreatedt(t2);
		c2.setSession(q.getSession().getCode());
		c2.setStatus(1);
		c2.setQuestion(q);
		c2.setOriginal(c);
		mapper.insertComment(c2);
		
		Comment rc2=mapper.findOne(c2.getId());
		assertNotNull(rc2.getOriginal());
		
		
		List<Comment> cl=mapper.findForQuestion(q.getId());
		assertNotNull(cl);
		assertEquals(2, cl.size());

		assertNotNull(cl.get(0).getComment());
		assertNotNull(cl.get(0).getSession());
		assertNotNull(cl.get(0).getOriginal());
		
		
		Comment rc1=mapper.findOne(c.getId());
		assertNotNull(rc1);
		assertNotNull(rc1.getComment());
		assertNotNull(rc1.getSession());
		assertNotNull(rc1.getQuestion());
		rc1.setStatus(1);
		rc1.setComment("valami más");
		mapper.updateComment(rc1);
		
		Comment c3=mapper.findOne(rc1.getId());
		assertEquals("valami más", c3.getComment());
		
		
	}
}
