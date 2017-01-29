package com.qumla.web.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FileControllerTest {
	private FileController fc;
	@Before
	public void setup(){
		fc=new FileController();
	}
	@Test
	public void testDarken() {
		String[] colors=new String[]{"100","100","100"};
		String result=String.join(",", fc.darken(colors, 0.1F));
		
	}

}
