package com.qumla.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qumla.domain.answer.Answer;
import com.qumla.domain.answer.AnswerStat;
import com.qumla.domain.answer.AnswerStatHistory;
import com.qumla.domain.answer.AnswerStatLocation;
import com.qumla.domain.answer.AnswerStatOption;
import com.qumla.domain.answer.CountryResult;
import com.qumla.domain.answer.LocationResult;
import com.qumla.service.AnswerService;

public interface AnswerDaoMapper extends AnswerService {
	public Answer findOneAnswer(Long id);
	
	public List<Answer> findOneByQuestionAndSession(@Param("session") String session,@Param("question") Long question);

	public List<Answer> findAnswerForQuestion(@Param("qid") Long qid);
	public int insertAnswer(Answer entity);
	public int updateAnswer(Answer entity); 

	public void deleteAnswer(Long id);	

	public AnswerStat findOneAnswerStat(Long id);
	public long getAnswerStatCountForQuestion(Long qid);
	
	
	public List<AnswerStat> findAnswerStatForQuestion(Long qid);

	
	
	public int saveAnswerStat(AnswerStat entity) throws SQLException;
	public void updateAnswerStatById(AnswerStat entity);
	public int 	incrementAnswerStatByFields(AnswerStat entity);
	
	public void deleteAnswerStat(Long id);	
	
	/* AnswerStatOption / AnswerStatLocation */
	public int saveAnswerStatLocation(AnswerStatLocation entity) throws SQLException;
	public int incrementAnswerStatLocationByFields(AnswerStatLocation entity);
	public int saveAnswerStatOption(AnswerStatOption entity) throws SQLException;
	public int incrementAnswerStatOptionByFields(AnswerStatOption entity);
	public List<AnswerStatOption> findAnswerStatOptionForQuestion(Long qid);
	public List<AnswerStatLocation> findAnswerStatLocationForQuestion(Long qid);
	
	/* Statistics */
	public List<LocationResult> locationResult(@Param("qid") Long qid, @Param("country") String country);
	public List<LocationResult> votingArea(@Param("qid") Long qid, @Param("country") String country);
	
	public List<LocationResult> regionResult(@Param("qid") Long qid, @Param("country") String country);
	public List<LocationResult> regionOptionResult(@Param("qid") Long qid,@Param("optionid") Long oid, @Param("country") String country);
	public List<LocationResult> locationOptionResult(@Param("qid") Long qid,@Param("optionid") Long oid, @Param("country") String country);

	
	public List<CountryResult> countryResult(@Param("qid") Long qid);
	
	public List<AnswerStatHistory> answerStatHistory(@Param("qid") Long qid);
	
	public int setAnswerIndexed(Answer as);
	public void resetIndexedAnswersForQuestion(@Param("qid") Long qid);
	public void resetAnswerStatForQuestion(@Param("qid") Long qid);
	public void resetAnswerStatLocationForQuestion(@Param("qid") Long qid);
	public void resetAnswerStatOptionForQuestion(@Param("qid") Long qid);
	public void updateLocationForAnswers(@Param("qid") Long qid);
	
	
}
