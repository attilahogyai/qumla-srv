package com.qumla.util;

import org.junit.Test;
import org.springframework.util.Assert;

public class BBCodeParserTest {

	@Test
	public void testParseString() {
		String result=BBCodeParser.parseString(" :) \n\n\n <b>"); 
		Assert.isTrue(result.equals(" <i class=\"material-icons\">mood</i> <br/> &lt;b&gt;"));
	}

}
