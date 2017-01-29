package com.qumla.service.impl;

import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qumla.domain.user.Session;
import com.qumla.domain.user.Useracc;
import com.qumla.service.LoginService;
@Component
public class LoginServiceImpl implements LoginService , 
ResourceRepository<Session, Long>{
	final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Autowired
	private LoginDaoMapper loginServiceMapper;
	@Autowired
	private UseraccDaoMapper userDaoMapper;
	
	
	@Override
	public Useracc getUseraccByLogin(String login) {
		return loginServiceMapper.getUseraccByLogin(login);
	}
	@Override
	public Useracc getUseraccByEmail(String login) {
		return loginServiceMapper.getUseraccByEmail(login);
	}

	@Override
	public Session getSession(String code) {
		return loginServiceMapper.getSession(code);
	}
	@Override
	public void insertSession(Session session){
		loginServiceMapper.insertSession(session);
	}
	@Override
	public void updateSession(Session session){
		loginServiceMapper.updateSession(session);
	}	
	
	@Override
	public Session updateFingerprint(Session session,String fingerPrint){
		if(fingerPrint!=null){
			Session s=loginServiceMapper.getSessionFingerprint(fingerPrint);
			if(s!=null){ // session already exist for fingerprint 
				logger.debug("move from session["+session.getCode()+"] to ["+s.getCode()+"]");
				session=s;
			}
			if(session.getFingerprint()==null){ // we do not update an existing session fingerprint
				session.setFingerprint(fingerPrint);
				loginServiceMapper.updateFingerprint(session);
			}
		}
		return session;
	}

	@Override
	public void createUser(Useracc user) {
		userDaoMapper.insertUser(user);
	}



	@Override
	public Session findOne(Long id, QueryParams requestParams) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Session> findAll(QueryParams requestParams) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterable<Session> findAll(Iterable<Long> ids,
			QueryParams requestParams) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends Session> S save(S entity) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
		
		
	}
	@Override
	public Session getSessionForUser(Long id) {
		return loginServiceMapper.getSessionForUser(id);
	}	
	
}
