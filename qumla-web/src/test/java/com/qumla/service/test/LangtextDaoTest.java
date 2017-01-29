package com.qumla.service.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.qumla.domain.dictionary.Langtext;
import com.qumla.domain.dictionary.LangtextRq;
import com.qumla.service.impl.LangtextDaoMapper;
import com.qumla.util.MailHelper;


public class LangtextDaoTest extends AbstractTest {

	@Before
	public void before() {
		super.before(LangtextDaoMapper.class);
	}
	
	@Test
	public void testGetDictionaryItems() throws Exception {
		LangtextRq request = new LangtextRq();
		request.setLang("en");
		LangtextDaoMapper mapper=(LangtextDaoMapper)getMapper(LangtextDaoMapper.class);
		List<Langtext> items = mapper.findAll();
		assertTrue(items.size() > 1);
		assertNotNull(items);
	}
	@Test
	public void parserTest(){
		LangtextDaoMapper mapper=(LangtextDaoMapper)getMapper(LangtextDaoMapper.class);
		String s=mapper.getText("email", "header", "en");
//		String s="<table cellpadding=\"10\" cellspacing=\"0\" border=\"0\"\n\r "+
//				"width=\"100%\" style=\"border-collapse:collapse;\"><tbody>\n\r"+
//		"<tr valign=\"top\"> \n\r"+			
//		"<td valign=\"top\" style=\"line-height:130%;color:rgb(150,150,150)\"> \n\r	"+
//		"<div style=\"color:rgb(150,150,150)\"> 	\n\r		"+
//		"<span style=\"text-decoration:none;font-size:14px;color:rgb(150,150,150);line-height:130%\"> \n\r	"+
//		"<span style=\"font-family:arial,helvetica,sans-serif;color:rgb(150,150,150);line-height:130%\"> "+	
//		"<a href=\"https://qumla.com\" target=\"_blank\">"+
//		"<img src=\"https://qumla.com/images/qumla-live.png\" style=\"vertical-align:middle\" width=\"100px\" style=\"width:100px\"></a>";
		Map<String, String> fileMap =new HashMap<String, String>();
		StringBuffer result=MailHelper.parseImages(s, fileMap);
		Assert.assertTrue(fileMap.size()>0);
	}
	
}
