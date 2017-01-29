package com.qumla.service.impl;

import java.util.List;

import com.qumla.domain.PagingFilter;
import com.qumla.domain.question.Option;
import com.qumla.domain.question.Question;
import com.qumla.domain.search.SearchResult;

public interface QuestionDaoMapper {
	public List<Question> findPopularQuestion(PagingFilter filter);
	public long findPopularQuestionCount(PagingFilter filter);
	
	public List<SearchResult> freeTextQueryQuestion(PagingFilter filter);
	public List<Question> queryQuestion(PagingFilter filter);
	public long queryQuestionCount(PagingFilter filter);
	
	public List<Question> findSessionFilterQuestion(PagingFilter filter);
	public long findSessionFilterQuestionCount(PagingFilter filter);

	public List<Question> findAsweredQuestion(PagingFilter filter);
	public long findAsweredQuestionCount(PagingFilter filter);
	
	public List<Question> findLatestQuestion(PagingFilter filter);
	public long findLatestQuestionCount(PagingFilter filter);
	
	
	public Question findOneQuestion(PagingFilter filter);
	public Question findOneQuestionForSession(PagingFilter filter);
	
	public List<Question> findTagQuestion(PagingFilter filter);
	public long findTagQuestionCount(PagingFilter filter);
	
	
	
	public Option findOneOption(Long id);

	public long getQuestionCountryCount(PagingFilter filter);

	
	
	public void insertQuestion(Question question);
	public void insertOption(Option option);
	
	public void deleteQuestion(Question question);

	public void updateQuestion(Question question);
	public void updateQuestionStatus(Question question);
	public void updateOption(Option option);
	public void deleteOption(Option option);
	public void deleteQuestionOptions(Question question);
	
	public void incrementAnswerCount(long id);
	
	
}
