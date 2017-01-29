package eu.dorsum.webtrader.services.news.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import eu.dorsum.webtrader.domain.news.News;
import eu.dorsum.webtrader.domain.news.NewsRq;
import eu.dorsum.webtrader.services.news.NewsService;

public class NewsServiceMyBatisImpl implements NewsService {
	
	@Autowired
	@Qualifier("newsMapper")
	private NewsMapper newsMapper;
	
	@Override
	public List<News> getNews(NewsRq request) {
		return newsMapper.getNews(request);
	}

	@Override
	public void createNews(News news) throws Exception {
		newsMapper.createNews(news);
	}	
	
	@Override
	public void modifyNews(News news) throws Exception {
		newsMapper.modifyNews(news);
	}
	
	@Override
	public void deleteNews(News news) throws Exception {
		newsMapper.deleteNews(news);
	}

}
