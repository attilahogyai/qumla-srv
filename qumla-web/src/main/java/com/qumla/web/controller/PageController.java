package com.qumla.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qumla.domain.question.QuestionFilter;
import com.qumla.service.impl.QuestionDaoMapper;

@Controller
public class PageController extends AbstractController{
	@Autowired
 	private QuestionDaoMapper questionMapper;

	@RequestMapping(value = "/profile/setup", method = RequestMethod.GET)
	@ResponseBody
    public Map<String,?> getProfileSetup(Authentication auth) {
		checkSession(auth);
		QuestionFilter qf=new QuestionFilter(RequestWrapper.getSession().getCountry());
		qf.setAnswered(true);
		qf.setSession(RequestWrapper.getSession().getCode());
		long answered=questionMapper.findAsweredQuestionCount(qf);
		long sessionQuestion=questionMapper.findSessionFilterQuestionCount(qf);
		return buildMap("answered", answered, "ownquestion",sessionQuestion);
	}
}

