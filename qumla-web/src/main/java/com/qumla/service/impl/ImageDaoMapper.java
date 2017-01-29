package com.qumla.service.impl;

import java.util.List;

import com.qumla.domain.image.Image;
import com.qumla.domain.image.ImageFilter;

public interface ImageDaoMapper {
	public Image findOne(Long id);
	public Image findByPath(String path);

	
	public List<Image> findAll();
	public List<Image> findForQuery(ImageFilter filter);
	public List<Image> findDefault(ImageFilter filter);
		
	public int insertImage(Image image);
	public int updateImage(Image image);
	
}
