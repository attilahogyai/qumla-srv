package eu.dorsum.webtrader.services.login.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.dorsum.webtrader.domain.login.OperatorPassword;
import eu.dorsum.webtrader.domain.login.PasswordExpirationData;
import eu.dorsum.webtrader.domain.login.PasswordSetting;
import eu.dorsum.webtrader.domain.user.UserLoginRq;
import eu.dorsum.webtrader.services.dictionary.impl.DictionaryMapper;

/**
 * (Local) helper services for WebtraderLoginModule
 */
@Service
public class WebtraderLoginService {

	@Autowired
	private DictionaryMapper directoryMapper;
	
	@Autowired
	private LoginModuleMapper mapper;

	public List<PasswordSetting> getUasProperties() {
		return mapper.getUasProperties();
	}

	public PasswordExpirationData getPasswordExpirationData(String opCode) {
		return mapper.getPasswordExpirationData(opCode);
	}

	public OperatorPassword getOperatorPassword(String opCode) {
		return mapper.getOperatorPassword(opCode);
	}

	public void createSession(UserLoginRq req) {
		mapper.createSession(req);
		mapper.updateOperatorOnLoginSuccess(req);
	}

	public void updateFailedAttempts(String opCode, int allowedLoginAttempts) {
		mapper.updateFailedAttempts(opCode, allowedLoginAttempts);
	}

	public void closeSession(String opCode) {
		mapper.closeSession(opCode);
	}

	public void updatePassword(OperatorPassword newOperatorPassword) {
		mapper.closeOldPassword(newOperatorPassword);
		mapper.updatePassword(newOperatorPassword);
		mapper.updatePasswordChangeDate(newOperatorPassword);
	}
	
}
