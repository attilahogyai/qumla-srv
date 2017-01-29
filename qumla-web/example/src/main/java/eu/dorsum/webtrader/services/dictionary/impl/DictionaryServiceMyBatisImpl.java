package eu.dorsum.webtrader.services.dictionary.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import eu.dorsum.webtrader.domain.dictionary.DictionaryItem;
import eu.dorsum.webtrader.domain.dictionary.DictionaryItemRq;
import eu.dorsum.webtrader.services.dictionary.DictionaryService;

public class DictionaryServiceMyBatisImpl implements DictionaryService {
	
	@Autowired
	@Qualifier("dictionaryMapper")
	private DictionaryMapper dictionaryMapper;

	@Override
	public List<DictionaryItem> getDictionaryItems(DictionaryItemRq request) {
		return dictionaryMapper.getDictionaryItems(request);
	}

	@Override
	public Integer getLanguageId(String language) {
		return dictionaryMapper.getLanguageId(language);
	}

	@Override
	public List<String> getLanguages() {
		return dictionaryMapper.getLanguages();
	}

	@Override
	public DictionaryItem updateItem(DictionaryItem item) {
		dictionaryMapper.updateItemInDb(item);
		return item;
	}

	@Override
	public DictionaryItem insertItem(DictionaryItem dictionaryItem) {
		dictionaryMapper.insertItemToDb(dictionaryItem);
		return dictionaryItem;
	}

	@Override
	public DictionaryItem deleteItem(DictionaryItem item) {
		dictionaryMapper.deleteItemFromDb(item);
		return item;
	}
}
