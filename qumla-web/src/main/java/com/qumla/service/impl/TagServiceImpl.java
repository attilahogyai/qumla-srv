package com.qumla.service.impl;

import java.awt.print.PageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.MetaRepository;
import io.katharsis.repository.ResourceRepository;
import io.katharsis.response.MetaInformation;

import org.apache.catalina.manager.util.BaseSessionComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.Metadata;
import com.qumla.domain.PagingFilter;
import com.qumla.domain.question.Question;
import com.qumla.domain.question.QuestionFilter;
import com.qumla.domain.tag.Tag;
import com.qumla.web.controller.AbstractController;
import com.qumla.web.controller.RequestWrapper;
@Component(value="tagServiceImpl")
public class TagServiceImpl extends AbstractService implements ResourceRepository<Tag, Long>, MetaRepository<Tag>{
	
	private Pattern tagPattern=Pattern.compile("\\B#(\\w*[A-Za-z_]+\\w*)");
	@Autowired
	private QuestionDaoMapper questionDaoMapper;	
	@Autowired
	private LocationDataDaoMapper locationDataDao;
	
	@Autowired
	private TagDaoMapper mapper;
	@Override
	public Tag findOne(Long id, QueryParams queryParams) {
		mapper.findOne(id);
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Tag> findAll(QueryParams queryParams) {

		PagingFilter filter=buildDefaultFilter(queryParams, new PagingFilter(RequestWrapper.getSession().getCountry()));
		if(filter.isPopular()){
			AbstractController.setInternationalFlag(locationDataDao,questionDaoMapper,filter);

			return mapper.getPopularTags(filter);
		}else if(filter.getQuery()!=null){
			return mapper.queryTags(filter);
		}
		throw new UnsupportedOperationException();
	}
	@Override
	public MetaInformation getMetaInformation(Iterable<Tag> resources, QueryParams queryParams) {
		PagingFilter filter=buildDefaultFilter(queryParams, new PagingFilter(RequestWrapper.getSession().getCountry()));
		long count=0;
		if(filter.isPopular()){
			count=mapper.getPopularTagsCount(filter);
		}else if(filter.getQuery()!=null){
			count=mapper.queryTagsCount(filter);			
		}
		Metadata m=new Metadata();
		m.setCount(count);
		m.setTotalPages((long)Math.ceil((double)count/filter.getPerPage()));
		return m;
		
	}
	@Override
	public Iterable<Tag> findAll(Iterable<Long> ids, QueryParams queryParams) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends Tag> S save(S entity) {
		if(entity.getId()==null){
			mapper.insertTag(entity);
		}
		return entity;
	}
	
	public void setTags(Set<String> tags, String country, String language){
		for (String tag : tags) {
			int r=incrementTag(tag, country);
			if(r==0){
				Tag t=new Tag();
				t.setTag(tag);
				t.setCount(1);
				t.setCountry(country);
				t.setLanguage(language);
				save(t);
			}
		}
	}
	public void unsetTags(Set<String> tags, String country){
		for (String tag : tags) {
			decrementTag(tag, country);
		}
	}
	
	private int incrementTag(String tag, String country){
		
		return mapper.incrementTag(tag, country);
	}
	private int decrementTag(String tag, String country){
		return mapper.decrementTag(tag, country);
	}	
	@Override
	public void delete(Long id) {
		throw new UnsupportedOperationException();
	}
	public Set<String> parseTags(String source){
		Matcher matcher=tagPattern.matcher(source);
		Set<String> tags=new HashSet<String>();
		while(matcher.find()){
			String g=matcher.group();
			tags.add(g);
		}
 		return tags;
	}

	public TagDaoMapper getMapper() {
		return mapper;
	}

	public void setMapper(TagDaoMapper mapper) {
		this.mapper = mapper;
	}
	
}
