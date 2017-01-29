package com.qumla.scheduler;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qumla.service.impl.QuestionDaoMapper;

public class CountryStatScheduler implements Callable<Object> {
	private static final Logger log = LoggerFactory.getLogger(CountryStatScheduler.class);
	
	@Autowired
	private QuestionDaoMapper questionDaoMapper;
	
	
	@Override
	public Object call() throws Exception {
		return null;
	}

	public void init() {
		/**
		ScheduledFuture<CountryStatScheduler> schedule = reloadEventsScheduler.schedule(this, 1000,
				TimeUnit.MILLISECONDS);
		try {
			log.debug("reschedule events:" + schedule.get());
		} catch (Exception e) {
			log.error("execution Exception", e);
		}
		**/
	}
}
