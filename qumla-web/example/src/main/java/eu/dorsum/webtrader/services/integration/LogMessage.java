package eu.dorsum.webtrader.services.integration;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;

public class LogMessage {
	
	private static final Logger logger = Logger.getLogger(LogMessage.class);

	public void log(Exchange exchange) {
		logger.debug("in message:" + exchange.getIn().getBody());
		if (exchange.hasOut()) {
			logger.debug("out message:" + exchange.getOut().getBody());
		}
	}
}
