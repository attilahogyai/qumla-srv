package com.qumla.web.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import com.qumla.domain.user.Session;
import com.qumla.service.LocationDataService;
import com.qumla.service.LocationService;
import com.qumla.service.LoginService;
import com.qumla.util.GeolocationDataUtil;
import com.qumla.util.GeolocationUtil;
import com.qumla.web.controller.RequestWrapper;

@Component
@Scope(value = "singleton")
public class WebSecurityContextRepository implements SecurityContextRepository {
	private static final Logger log = LoggerFactory
			.getLogger(WebSecurityContextRepository.class);
	public static final String WTS_SESSION = "wts_session";
	
	
	@Autowired
	private LoginService loginService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private LocationDataService locationDataService;
	
	private GeolocationUtil geoUtil;
	private GeolocationDataUtil geoDataUtil;
	private static final List<String> tokens=Collections.synchronizedList(new ArrayList<String>(1000));
	

	@Override
	public SecurityContext loadContext(
			HttpRequestResponseHolder requestResponseHolder) {
		
		if(geoDataUtil==null){
			geoDataUtil=new GeolocationDataUtil(locationDataService);
		}
		if(geoUtil==null){
			geoUtil=new GeolocationUtil(locationService);
		}		
		HttpServletRequest req = requestResponseHolder.getRequest();
		RequestWrapper.httpRequest.set(req);
		String authorization = req.getHeader("Authorization");
		if (authorization != null &&
				!authorization.equals("Bearer null") && 
				authorization.length() > 6) {
			String token = authorization.substring(7);
			synchronized (tokens) {
				int i=tokens.indexOf(token);
				if(i>-1){
					token=tokens.get(i);
				}else{
					tokens.add(token);
					if(tokens.size()>1000){
						tokens.remove(0);
					}				
				}
			}
			log.debug("tokens size:"+tokens.size());
			Session session = loginService.getSession(token);
			if (session != null) {
				if (session.getValid() == 1) {

					List<GrantedAuthority> grantedList = new ArrayList<GrantedAuthority>(
							session.getScopes().length);
					for (int i = 0; i < session.getScopes().length; i++) {
						log.debug("build user grant list:"
								+ session.getScopes()[i]);
						grantedList.add(new SimpleGrantedAuthority(session
								.getScopes()[i]));
					}
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
							session, null, grantedList);
					SecurityContextHolder.getContext().setAuthentication(auth);
					Date oldPing=session.getLastPing();
					session.setLastPing(new Date());
					
					if(req.getParameter("l")!=null){
						session.setLanguage(req.getParameter("l"));
					}
					String ip = RequestWrapper.getRemoteIP(req);
					if(ip!=null && ip.equals("127.0.0.1")){
						session.setCountry("HU");
						session.setCity("Budapest");
					}					
					if (session.getCountry() == null || session.getCity() == null || session.getLocation()==null) {
						try {
							geoDataUtil.updateCountryCity(session, ip);
							updateSession(token, session);
						} catch (Exception e1) {
							log.error("unable to update session.",e1);
						}
						final String finalToken=token;
						
						Runnable updateT = new Runnable() {
							@Override
							public void run() {
								try {
									geoDataUtil.updateSessionLocation(session);
									updateSession(finalToken, session);
								} catch (IOException e) {
									log.error("query country error:"
											+ e.getMessage());
								}
							}
						};
						Thread t = new Thread(updateT);
						t.start();
					} else {
						if(oldPing==null || new Date().getTime() - oldPing.getTime()>120000){
							updateSession(token, session);
						}
					}
					req.setAttribute(WTS_SESSION, session);

					log.debug("setAuthenctication for context");
					return SecurityContextHolder.getContext();
				}
			}else{
				log.debug("session["+authorization+"] is not valid");
			}
		}
		return SecurityContextHolder.getContext();
		//
		// SessionCache
		// sessionCache=(SessionCache)req.getSession().getAttribute(WTS_SESSION);
		// if(sessionCache!=null){
		// UsernamePasswordAuthenticationToken auth=new
		// UsernamePasswordAuthenticationToken(sessionCache,null,
		// (List<GrantedAuthority>)sessionCache.getGrantedAuthorities());
		// SecurityContextHolder.getContext().setAuthentication(auth);
		// return SecurityContextHolder.getContext();
		// }
		// return SecurityContextHolder.getContext();
	}

	private void updateSession(String token, Session session) {
		synchronized (token) {
			log.debug("start update session:"+token);
			loginService.updateSession(session);
			log.debug("end update session:"+token);
		}
	}

	@Override
	public void saveContext(SecurityContext context,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO cache token
		// Cache sessionCache=CacheHandler.getCache(TOKENCACHE);
		// sessionCache.put(element);
		// new Element(token,session);
		// SecurityContextHolder.getContext().getAuthentication()

	}

	// public SessionCache createSessionCache(String id){
	// SessionCache sc=new SessionCache(id);
	// sc.setClient(id);
	// List<GrantedAuthority> grantedAuthority=new
	// ArrayList<GrantedAuthority>();
	// grantedAuthority.add(new SimpleGrantedAuthority("ROLE_USER"));
	// sc.setGrantedAuthorities(grantedAuthority);
	// return sc;
	// }

	@Override
	public boolean containsContext(HttpServletRequest request) {
		if (request.getAttribute(WTS_SESSION) != null) {
			return true;
		}
		return false;
	}

}
