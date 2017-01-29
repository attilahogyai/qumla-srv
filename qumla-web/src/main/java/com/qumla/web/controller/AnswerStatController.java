package com.qumla.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qumla.domain.answer.AnswerStatHistory;
import com.qumla.service.impl.AnswerDaoMapper;

@Controller
public class AnswerStatController {
	@Autowired
	private AnswerDaoMapper answerDaoMapper;
	
	@RequestMapping(value="/answerstathistory",method=RequestMethod.GET)
	@ResponseBody
	public Object getAnswerStatHistory(@RequestParam("qid") Long qid,@RequestParam("country") String country, Authentication auth){
		List<AnswerStatHistory> result=(List<AnswerStatHistory>)answerDaoMapper.answerStatHistory(qid);
		
		return result;
	}
}
