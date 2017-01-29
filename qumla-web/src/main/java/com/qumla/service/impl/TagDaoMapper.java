package com.qumla.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qumla.domain.PagingFilter;
import com.qumla.domain.search.SearchResult;
import com.qumla.domain.tag.Tag;

public interface TagDaoMapper {
	public Tag findOne(Long id); 
	public void insertTag(Tag tag);
	public int incrementTag(@Param("tag") String tag,@Param("country") String country);
	public int decrementTag(@Param("tag") String tag,@Param("country") String country);
	public List<Tag> queryTags(PagingFilter filter);
	public List<SearchResult> freeTextQueryTag(PagingFilter filter);
	
	
	public long queryTagsCount(PagingFilter filter);
	
	public List<Tag> getPopularTags(PagingFilter filter);
	public long getPopularTagsCount(PagingFilter filter);
	
}
