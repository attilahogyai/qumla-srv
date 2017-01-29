package eu.dorsum.webtrader.services.dictionary.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.dorsum.webtrader.domain.dictionary.DictionaryItem;
import eu.dorsum.webtrader.domain.dictionary.DictionaryItemRq;
import eu.dorsum.webtrader.services.test.AbstractMyBatisMapperTest;

public class DictionaryMapperTest extends AbstractMyBatisMapperTest<DictionaryMapper> {

	@Before
	public void before() {
		initDb(DictionaryMapper.class, TestDb.values());
	}
	
	@Test
	public void testGetDictionaryItems() throws Exception {
		DictionaryItemRq request = new DictionaryItemRq();
		request.setLang("en");
		for (DictionaryMapper mapper : mappers.values()) {
			List<DictionaryItem> items = mapper.getDictionaryItems(request);
			assertTrue(items.size() > 1);
			assertNotNull(items);
		}
	}
	
	@Test
	public void testGetLanguageId() throws Exception {
		for (DictionaryMapper mapper : mappers.values()) {
			Integer languageId = mapper.getLanguageId("en");
			assertNotNull(languageId);
		}
	}
	
	@Test
	public void testGetLanguages() throws Exception {
		for (DictionaryMapper mapper : mappers.values()) {
			List<String> languages = mapper.getLanguages();
			assertNotNull(languages);
			assertTrue(languages.size() > 0);
		}
	}
	
	@Test
	public void testInsertItemToDb() throws Exception {
		for (DictionaryMapper mapper : mappers.values()) {
			DictionaryItem item = new DictionaryItem();
			item.setLanguage("hu");
			item.setCode("UNITTEST");
			item.setDescription("From junit test");
			mapper.insertItemToDb(item);
		}
	}
	
	@Test
	public void testUpdateItemInDb() throws Exception {
		for (DictionaryMapper mapper : mappers.values()) {
			DictionaryItem item = new DictionaryItem();
			item.setLanguage("hu");
			item.setCode("UNITTEST2");
			item.setDescription("From junit test");
			mapper.updateItemInDb(item);
		}
	}
	
	@Test
	public void testDeleteItemFromDb() throws Exception {
		for (DictionaryMapper mapper : mappers.values()) {
			DictionaryItem item = new DictionaryItem();
			item.setCode("NOTEXISTS");
			mapper.deleteItemFromDb(item);
		}
	}
	
}
