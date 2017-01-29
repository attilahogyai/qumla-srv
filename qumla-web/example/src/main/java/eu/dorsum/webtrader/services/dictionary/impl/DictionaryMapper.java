package eu.dorsum.webtrader.services.dictionary.impl;

import eu.dorsum.webtrader.domain.dictionary.DictionaryItem;
import eu.dorsum.webtrader.services.dictionary.DictionaryService;

public interface DictionaryMapper extends DictionaryService {

	public void updateItemInDb(DictionaryItem item);

	public void insertItemToDb(DictionaryItem item);
	
	public void deleteItemFromDb(DictionaryItem item);
	
}
