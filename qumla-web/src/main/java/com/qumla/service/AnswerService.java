package com.qumla.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qumla.domain.answer.Answer;

public interface AnswerService {

	public <S extends Answer> void updateAnswerStat(S entity);

	public List<Answer> findAnswerForQuestion(@Param("qid") Long qid);
	public void resetStatForQuestion(@Param("qid") Long qid);
}
