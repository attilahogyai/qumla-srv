package com.qumla.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qumla.domain.answer.Answer;
import com.qumla.domain.answer.CountryResult;
import com.qumla.domain.answer.LocationResult;
import com.qumla.domain.location.Country;
import com.qumla.domain.location.LocationData;
import com.qumla.domain.question.Question;
import com.qumla.domain.user.Session;
import com.qumla.service.impl.AnswerDaoMapper;
import com.qumla.service.impl.LocationDataDaoMapper;
import com.qumla.service.impl.QuestionDaoMapper;
import com.qumla.service.impl.QuestionServiceImpl;
import com.qumla.web.exception.AccessDenied;

@Controller
public class LocationController extends AbstractController {
	@Autowired
	private AnswerDaoMapper answerServiceMapper;
	@Autowired
	private LocationDataDaoMapper locationDataDao;
	@Autowired
	private QuestionServiceImpl questionServiceImpl;

	@RequestMapping(value="/locationresult",method=RequestMethod.GET)
	@ResponseBody
	public Object getLocationResult(@RequestParam("qid") Long qid,@RequestParam("country") String country, Authentication auth){
		List<LocationResult> result=(List<LocationResult>)answerServiceMapper.locationResult(qid, country);
		Long sum=0L;
		List<LocationResult> grouped=new ArrayList<LocationResult>();
		LocationResult actGroup=null; 
		for (LocationResult res : result) {
			if(actGroup==null || !actGroup.getLocation().equals(res.getLocation())){
				actGroup=(LocationResult)res.clone();
				actGroup.setMax(actGroup.getCount());
				grouped.add(actGroup);
			}else{
				actGroup.setCount(actGroup.getCount()+res.getCount());
				if(actGroup.getMax()<res.getCount()){
					actGroup.setOption(res.getOption());
					actGroup.setMax(res.getCount());
					actGroup.setText(res.getText());
				}
			}
			sum+=(Long)res.getCount();
		}
		for (LocationResult res : grouped) {
			res.setPercent(((float)res.getCount())/sum*100);
			res.setTotal(sum);
		}		
		return grouped;
	}
	@RequestMapping(value="/regionresult",method=RequestMethod.GET)
	@ResponseBody
	public Object getRegionResult(@RequestParam("qid") Long qid,@RequestParam("country") String country, Authentication auth){
		Question question = questionServiceImpl.findOne(qid, null);
		if(question.getDashboard() == Question.DASHBOARD_ADVANCED || RequestWrapper.getSession().isCustomer()){
			List<LocationResult> result=(List<LocationResult>)answerServiceMapper.regionResult(qid, country);
			Long sum=0L;
			List<LocationResult> grouped=new ArrayList<LocationResult>();
			LocationResult actGroup=null; 
			for (LocationResult res : result) {
				if(res.getName()==null) continue;
				if(actGroup==null || !actGroup.getName().equals(res.getName())){
					actGroup=(LocationResult)res.clone();
					actGroup.setMax(actGroup.getCount());
					grouped.add(actGroup);
				}else{
					actGroup.setCount(actGroup.getCount()+res.getCount());
					if(actGroup.getMax()<res.getCount()){
						actGroup.setOption(res.getOption());
						actGroup.setMax(res.getCount());
						actGroup.setText(res.getText());
					}
				}
				sum+=(Long)res.getCount();
			}
			for (LocationResult res : grouped) {
				res.setPercent(((float)res.getCount())/sum*100);
				res.setTotal(sum);
			}		
			return grouped;
		}else{
			throw new AccessDenied();
		}
	}
	@RequestMapping(value="/votingarea",method=RequestMethod.GET)
	@ResponseBody
	public Object getOptionRegionResult(@RequestParam("qid") Long qid, Authentication auth){
		Session session=RequestWrapper.getSession();
		Question question=questionServiceImpl.findOne(qid, null);
		List<Answer> a=answerServiceMapper.findOneByQuestionAndSession(session.getCode(), qid);
		List<LocationResult> result = new ArrayList();
		if(question.getStatus()!=50 && a==null){
			return result;
		}
		//result=(List<LocationResult>)answerServiceMapper.votingArea(qid, session.getCountry());		
		result=(List<LocationResult>)answerServiceMapper.votingArea(qid, null);
		return result;

	}	
	@RequestMapping(value="/myoptionregionresult",method=RequestMethod.GET)
	@ResponseBody
	public Object getMyOptionRegionResult(@RequestParam("qid") Long qid, Authentication auth){
		Session session=RequestWrapper.getSession();
		
		List<Answer> a=answerServiceMapper.findOneByQuestionAndSession(session.getCode(), qid);
		List<LocationResult> result = new ArrayList();
		if(a==null){
			return result;
		}
		result=(List<LocationResult>)answerServiceMapper.locationOptionResult(qid, a.get(0).getOption(), session.getCountry());		
		return result;

	}	
	@RequestMapping(value="/countryresult",method=RequestMethod.GET)
	@ResponseBody
	public Object getLocationResult(@RequestParam("qid") Long qid, Authentication auth){
		Question question = questionServiceImpl.findOne(qid, null);
		if(question.getDashboard() == Question.DASHBOARD_ADVANCED || RequestWrapper.getSession().isCustomer()){
			List<CountryResult> result=(List<CountryResult>)answerServiceMapper.countryResult(qid);
			Long sum=0L;
			List<CountryResult> grouped=new ArrayList<CountryResult>();
			CountryResult actGroup=null; 
			for (CountryResult res : result) {
				if(actGroup==null || !actGroup.getCountry().equals(res.getCountry())){
					actGroup=(CountryResult)res.clone();
					actGroup.setMax(actGroup.getCount());
					grouped.add(actGroup);
				}else{
					actGroup.setCount(actGroup.getCount()+res.getCount());
					if(actGroup.getMax()<res.getCount()){
						actGroup.setOption(res.getOption());
						actGroup.setMax(res.getCount());
						actGroup.setText(res.getText());
					}
				}
				sum+=(Long)res.getCount();
			}
			for (CountryResult res : grouped) {
				res.setPercent(((float)res.getCount())/sum*100);
				res.setTotal(sum);
			}		
			return grouped;
		}else{
			throw new AccessDenied();
		}

	}
	@RequestMapping(value="/location",method=RequestMethod.GET)
	@ResponseBody
	public Object location(HttpServletRequest request,HttpServletResponse response){
		Session session=RequestWrapper.getSession();
		Map<String,String> location = new HashMap<String,String>();
		if(session.getLocation()!=null){
			LocationData ld=locationDataDao.findOne(session.getLocation());
			if(ld!=null){
				location.put("lat", ld.getLat().toString());
				location.put("lon", ld.getLon().toString());
			}
		}
		Country c=locationDataDao.findCountry(session.getCountry());
		
		location.put("country", session.getCountry());
		location.put("language", session.getLanguage());
		location.put("city", session.getCity());
		location.put("defLang", c.getDefLang());
		return location;
	}
	
}
