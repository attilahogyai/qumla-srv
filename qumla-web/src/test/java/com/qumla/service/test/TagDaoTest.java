package com.qumla.service.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.qumla.domain.PagingFilter;
import com.qumla.domain.search.SearchResult;
import com.qumla.domain.tag.Tag;
import com.qumla.service.impl.TagDaoMapper;
import com.qumla.service.impl.TagServiceImpl;
import com.qumla.web.controller.RequestWrapper;

public class TagDaoTest extends AbstractTest{
	@Before
	public void before() {
		super.before(TagDaoMapper.class);
	}
	@Test
	public void saveTag(){
		TagDaoMapper mapper=(TagDaoMapper)getMapper(TagDaoMapper.class);
		Tag t=new Tag();
		t.setTag("c");
		t.setType(1);
		t.setCount(10);
		t.setCountry("HU");
		t.setLanguage("english");
		mapper.insertTag(t);
		
		Tag t2=mapper.findOne(t.getId());
		Assert.assertTrue(t2.getCount()==10);
		Assert.assertTrue(t2.getTag().equals("c"));
		Assert.assertTrue(t2.getType()==1);
		
		
	}
	@Test
	public void incrementDecrementTag(){
		TagDaoMapper mapper=(TagDaoMapper)getMapper(TagDaoMapper.class);
		mapper.incrementTag("#qumla","HU");
		mapper.decrementTag("#qumla","HU");
	}
	@Test
	public void incrementTag(){
		TagDaoMapper mapper=(TagDaoMapper)getMapper(TagDaoMapper.class);
		TagServiceImpl tsimp=new TagServiceImpl();
		tsimp.setMapper(mapper);
		Set<String> tags=new HashSet<String>();
		tags.add("#ttttttttt");
		tags.add("#ttttttttt2");
		tsimp.setTags(tags, "HU", "english");
		
		tags=new HashSet<String>();
		tags.add("#ttttttttt");
		tsimp.setTags(tags, "EN", "hungarian");
		tsimp.setTags(tags, "EN", "hungarian");
		
		PagingFilter pf=new PagingFilter(RequestWrapper.getSession().getCountry());
		pf.setQuery("#tttt");
		pf.setCountryCode("HU");
		List<SearchResult> sr=mapper.freeTextQueryTag(pf);
		Assert.assertTrue(sr.size()==3);
		pf.setQuery("#tttt");
		pf.setCountryCode("EN");
		sr=mapper.freeTextQueryTag(pf);
		Assert.assertTrue(sr.size()==3);
		boolean ok=false;
		for (SearchResult searchResult : sr) { // there should be one with count 2
			Tag t=mapper.findOne(searchResult.getId());
			if(t.getCount()==2) ok=true;
		}
		Assert.assertTrue(ok);

	}
	
	@Test
	public void testTag(){
		TagServiceImpl ts=new TagServiceImpl();
		Set<String> tags=ts.parseTags("dfs df sdf sdfs ");
		Assert.assertTrue(tags.size()==0);
		
		tags=ts.parseTags("dfs #df #4dd534 #33333 ");
		Assert.assertTrue(tags.size()==2);
		
	}
	@Test
	public void findTag(){
		TagDaoMapper mapper=(TagDaoMapper)getMapper(TagDaoMapper.class);
		PagingFilter pf=new PagingFilter(RequestWrapper.getSession().getCountry());
		pf.setPopular(true);
		List<Tag> popular=mapper.getPopularTags(pf);
		Assert.assertTrue(popular.size()>0);
		
		pf=new PagingFilter(RequestWrapper.getSession().getCountry());
		pf.setQuery("#qumla");
		List<Tag> result=mapper.queryTags(pf);
		Assert.assertTrue(result.size()>0);
	}
	@Test
	public void freeSearchTag(){
		TagDaoMapper mapper=(TagDaoMapper)getMapper(TagDaoMapper.class);
		PagingFilter pf=new PagingFilter(RequestWrapper.getSession().getCountry());
		pf=new PagingFilter(RequestWrapper.getSession().getCountry());
		pf.setQuery("#qumla");
		List<SearchResult> result=mapper.freeTextQueryTag(pf);
		Assert.assertTrue(result.size()>0);
	}	
	@Test
	public void parseTag(){
		
		TagServiceImpl ts=new TagServiceImpl();
		Set<String> tags=ts.parseTags("dfs df sdf sdfs ");
		Assert.assertTrue(tags.size()==0);
		
		tags=ts.parseTags("dfs #df sdf #4dd534 sdfs ");
		Assert.assertTrue(tags.size()==2);
		
	}
}
