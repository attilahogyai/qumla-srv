package com.qumla.web.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qumla.domain.PagingFilter;
import com.qumla.domain.location.Country;
import com.qumla.domain.question.Question;
import com.qumla.domain.question.QuestionFilter;
import com.qumla.domain.search.SearchResult;
import com.qumla.service.impl.LocationDataDaoMapper;
import com.qumla.service.impl.QuestionDaoMapper;
import com.qumla.service.impl.TagDaoMapper;

@Controller
public class SearchController extends AbstractController{
	@Autowired
	QuestionDaoMapper questionDaoMapper;
	@Autowired
	TagDaoMapper tagDaoMapper;
	@Autowired
	LocationDataDaoMapper locationDataMapper;
		
	@RequestMapping(value="/search",method=RequestMethod.POST)
	@ResponseBody
	public Object getLocationResult(@RequestParam("query") String query,@RequestParam("l") String language){
		if(query==null || query.length()<=2){
			return wrapResult(new ArrayList()); 
		}
		Set<SearchResult> result = new HashSet<SearchResult>();
		String searchLanguage=searchLanguageByCode(language);
		PagingFilter pf=new PagingFilter(RequestWrapper.getSession().getCountry());
		pf.setQuery(query);
		pf.setLanguage(searchLanguage);
		pf.setOffset(0);
		pf.setLimit(10);	
		// search based on country default language
		Country c=AbstractController.setInternationalFlag(locationDataMapper, questionDaoMapper, pf);
		pf.setLanguage(AbstractController.searchLanguageByCode(c.getDefLang()));
		List<SearchResult> r=(List<SearchResult>)questionDaoMapper.freeTextQueryQuestion(pf);		
		
		pf.setLanguage(searchLanguage);
		List<SearchResult> r2=(List<SearchResult>)questionDaoMapper.freeTextQueryQuestion(pf);
		
		result.addAll(r);
		result.addAll(r2);
		
		
		String queryArray[]=query.split(" ");
	
		for (int i = 0; i < queryArray.length; i++) {
			if(!queryArray[i].startsWith("#")) continue;			
			pf=new PagingFilter(RequestWrapper.getSession().getCountry());
			pf.setQuery(queryArray[i].trim());
			pf.setLanguage(searchLanguage);
			pf.setOffset(0);
			pf.setLimit(5);			
			r=(List<SearchResult>)tagDaoMapper.freeTextQueryTag(pf);		
			result.addAll(r);
		}
		return wrapResult(result);
	}


}
