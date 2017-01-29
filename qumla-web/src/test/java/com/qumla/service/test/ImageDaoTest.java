package com.qumla.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.qumla.domain.image.Image;
import com.qumla.service.impl.ImageDaoMapper;

public class ImageDaoTest extends AbstractTest {
	@Before
	public void before() {
		super.before(ImageDaoMapper.class);
	}
	@Test
	public void createImage(){
		ImageDaoMapper mapper=(ImageDaoMapper)getMapper(ImageDaoMapper.class);
		
		Image q=new Image();
		q.setPath("/image.png");
		q.setTitle("én nevem");
		q.setDominant("#fffffff");
		q.setType(2);
		q.setStatus(2);

		mapper.insertImage(q);
		
		Image retImg=mapper.findOne(q.getId());
		assertNotNull(retImg);
		
		assertNotNull(retImg.getId());
		assertNotNull(retImg.getDominant());
		assertNotNull(retImg.getPath());
		assertEquals("én nevem", retImg.getTitle());
		assertNotNull(retImg.getType());
		assertNotNull(retImg.getStatus());

		retImg.setStatus(1);
		mapper.updateImage(retImg);
		Image retImg2=mapper.findOne(retImg.getId());
		
		assertEquals((Integer)1, retImg2.getStatus());
		
	}
	
}
