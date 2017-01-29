package com.qumla.web.controller;

import io.katharsis.invoker.KatharsisInvokerBuilder;
import io.katharsis.locator.JsonServiceLocator;
import io.katharsis.rs.KatharsisProperties;
import io.katharsis.servlet.AbstractKatharsisFilter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class JsonApiFilter extends AbstractKatharsisFilter {

	public JsonApiFilter() {
		// TODO Auto-generated constructor stub
	}
	private String resourceSearchPackage;
    private String resourceDefaultDomain;

    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        resourceSearchPackage = filterConfig.getInitParameter(KatharsisProperties.RESOURCE_SEARCH_PACKAGE);
        resourceDefaultDomain = filterConfig.getInitParameter(KatharsisProperties.RESOURCE_DEFAULT_DOMAIN);
    }
    public String getResourceSearchPackage() {
        return resourceSearchPackage;
    }

    public void setResourceSearchPackage(String resourceSearchPackage) {
        this.resourceSearchPackage = resourceSearchPackage;
    }

    public String getResourceDefaultDomain() {
        return resourceDefaultDomain;
    }

    public void setResourceDefaultDomain(String resourceDefaultDomain) {
        this.resourceDefaultDomain = resourceDefaultDomain;
    }


	@Override
	protected KatharsisInvokerBuilder createKatharsisInvokerBuilder() {
	    return new KatharsisInvokerBuilder()
        .resourceSearchPackage(getResourceSearchPackage())
        .resourceDefaultDomain(getResourceDefaultDomain())
        .jsonServiceLocator(new JsonServiceLocator() {
            @Override
            public <T> T getInstance(Class<T> clazz) {
            	String beanName=clazz.getSimpleName();
            	beanName=beanName.substring(0, 1).toLowerCase().concat(beanName.substring(1));
                return (T)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean(beanName);
            }
        });
	}

}
