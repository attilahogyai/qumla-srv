package com.qumla.image;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ImageToolsTest {
	@Before
	public void setup(){
		System.setProperty("img.store", "D:\\munka\\src\\xprt\\qumla-server\\srv\\qumla-web\\src\\main\\resources\\");
        System.setProperty("bin.searchPath", "d:\\prog\\GraphicsMagick-1.3.21-Q8\\;d:\\prog\\ImageMagick-6.9.2-Q16\\");
	}
	//@Test
	public void testGetDominantColorsForSingleImage() {
		try {
			String color=ImageTools.getDominantColorsForSingleImage(ImageConfig.QUESTIONCONFIG, "94IMG_60591.JPG");
			
			assertTrue(color.split(",").length==3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//@Test
	public void testGetDimensionsForSingleImage() {
		try {
			Integer [] dim=ImageTools.getDimensionsForSingleImage(ImageConfig.QUESTIONCONFIG, "94IMG_60591.JPG");
			
			assertNotNull(dim[0]);
			assertNotNull(dim[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
