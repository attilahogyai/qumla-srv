package eu.dorsum.webtrader.util;

import java.util.Properties;

import org.apache.camel.spring.spi.BridgePropertyPlaceholderConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class SystemPropertyPlaceholderConfigurer extends BridgePropertyPlaceholderConfigurer {

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		System.getProperties().putAll(props);
	}

}