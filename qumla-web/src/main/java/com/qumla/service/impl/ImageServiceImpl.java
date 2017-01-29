package com.qumla.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.image.Image;
import com.qumla.domain.image.ImageFilter;
import com.qumla.util.TranslationUtil;
import com.qumla.web.controller.AbstractController;
import com.qumla.web.controller.RequestWrapper;
import com.qumla.web.exception.AccessDenied;

@Component("imageServiceImpl")
public class ImageServiceImpl extends AbstractService implements ResourceRepository<Image, Long>{
	private TranslationUtil translationUtil = new TranslationUtil();
	
	@Autowired
	private ImageDaoMapper imageDaoMapper;
	
	@Override
	public Image findOne(Long id, QueryParams requestParams) {
		return imageDaoMapper.findOne(id);		
	}
	@Override
	public Iterable<Image> findAll(Iterable<Long> ids,
			QueryParams requestParams) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Image> findAll(QueryParams requestParams) {
		ImageFilter filter=new ImageFilter(RequestWrapper.getSession().getCountry());	
		buildDefaultFilter(requestParams, filter);
		if(filter.getQuery()!=null){
			String language = AbstractController.searchLanguageByDesc(filter.getLanguage());
			String query = filter.getQuery();
			
			if(!language.equals("en")){
				query=translationUtil.translateText(query, language);
			}
			query=query.trim();
			query=query.replaceAll("&", "");
			
			while (query.indexOf("  ")>-1) {
				query=query.replaceAll("  ", " ");
			}
			query=query.replaceAll(" ","|");
			filter.setQuery(query);
			List<Image> images = new ArrayList<Image>();
			try{
				images=imageDaoMapper.findForQuery(filter);	
			}catch(Exception e){
				logger.debug("error getting result for translation:"+query);
			}
			if(images.size()<10){
				images.addAll(imageDaoMapper.findDefault(filter));	
			}
			return images;
		}else{
			return imageDaoMapper.findAll();				
		}
	}

	@Override
	public <S extends Image> S save(S entity) {
		if(entity.getId()==null){
			if(entity.getType()==null){
				entity.setType(0);
			}
			if(entity.getStatus()==null){
				entity.setStatus(2);
			}
			imageDaoMapper.insertImage(entity);
		}else{
			imageDaoMapper.updateImage(entity);
		}
		return entity;
		
	}
	public Image findByPath(String path){
		return imageDaoMapper.findByPath(path);
	}

	@Override
	public void delete(Long id) {
		throw new UnsupportedOperationException();
		
		
	}

}
