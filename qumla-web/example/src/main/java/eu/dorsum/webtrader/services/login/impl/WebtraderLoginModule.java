package eu.dorsum.webtrader.services.login.impl;

import hu.dorsum.clavis.uas.DefaultPasswordChecker;
import hu.dorsum.clavis.uas.db.SaltedDigestedPassword;
import hu.dorsum.clavis.uas.exception.FormatException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import eu.dorsum.webtrader.domain.login.OperatorPassword;
import eu.dorsum.webtrader.domain.login.PasswordExpirationData;
import eu.dorsum.webtrader.domain.login.PasswordSetting;
import eu.dorsum.webtrader.domain.user.UserLoginRq;

/**
 * This UasLoginModule implementation performs authentication from database.
 */
@Service
public class WebtraderLoginModule {

	private static final Logger log = Logger.getLogger(WebtraderLoginModule.class);
	private static final String PROPERTY_ALLOWED_LOGIN_ATTEMPTS = "allowed.login.attempts";
	private static final String PROPERTY_PASSWORD_EXPIRATION_DAYS = "password.expiration.days";

	@Autowired
	private WebtraderLoginService service;
	
	private Properties uasProperties;
	private int allowedLoginAttempts;

	@PostConstruct
	public void init() {
		List<PasswordSetting> settings = service.getUasProperties();

		uasProperties = new Properties();
		for (PasswordSetting setting : settings) {
			uasProperties.put(setting.getCode(), setting.getValue());
		}
		allowedLoginAttempts = Integer.parseInt(uasProperties.getProperty(PROPERTY_ALLOWED_LOGIN_ATTEMPTS));
	}

	public boolean isPasswordChangeRequired(String opCode) {
		int passwordExpirationDays = Integer.parseInt(uasProperties.getProperty(PROPERTY_PASSWORD_EXPIRATION_DAYS));

		try {
			PasswordExpirationData data = service.getPasswordExpirationData(opCode);
			if (data == null) {
				log.debug("expiration data is null");
				return false;
			}
			Integer force = data.getPasswdchgforced();
			if (force != null && force == 1) {
				log.debug("password change forced: " + force);
				return true;
			}
			Date lastpasswdchg = data.getLastpasswdchg();
			if (lastpasswdchg == null) {
				log.debug("lastpasswdchg is null");
				return true;
			}
			Date passwordEndDate = DateUtils.addDays(lastpasswdchg, passwordExpirationDays);
			Date now = new Date();
			if (passwordEndDate.before(now)) {
				log.debug("Password end date is before now: " + passwordEndDate);
				return true;
			}
			return false;
		} catch (DataAccessException e) {
			log.error("Error while checking password validity", e);
			throw new RuntimeException("Error while checking password validity");
		}
	}

	public boolean login(UserLoginRq req) throws LoginException {
		try {
			String opCode = req.getLogin();
			String opPassword = req.getPassword();
			
			OperatorPassword passwordData = service.getOperatorPassword(opCode);
			if (passwordData == null) {
				throw new FailedLoginException("Invalid credentials");
			}
			String digestedPassword = passwordData.getPassword();
			Integer salt = passwordData.getSalt();

			if (digestedPassword == null || salt == null) {
				throw new LoginException("Invalid credentials");
			}
			
			boolean authenticated;
			if (SaltedDigestedPassword.check(digestedPassword, opPassword, salt)) {
				authenticated = true;
				service.createSession(req);
			} else {
				authenticated = false;
				log.debug("password incorect");

				service.updateFailedAttempts(opCode, allowedLoginAttempts);
				throw new FailedLoginException("Invalid credentials");
			}
			return authenticated;
		} catch (NoSuchAlgorithmException | DataAccessException e) {
			log.error("Internal error during login", e);
			throw new LoginException("Internal error");
		} 
	}

	public boolean logout(String opCode) {
		service.closeSession(opCode);
		return true;
	}

	public void changePassword(String oldPassword, String newPassword, String userName) throws LoginException {
		try {
			DefaultPasswordChecker passwordChecker = new DefaultPasswordChecker();
			passwordChecker.setUasProperties(uasProperties);
			passwordChecker.afterPropertiesSet();
			passwordChecker.checkPassword(userName, oldPassword, newPassword);

			int salt = new SecureRandom().nextInt();
			
			String newDigestedPassword = new SaltedDigestedPassword().saltAndDigest(newPassword, salt);
			OperatorPassword newOperatorPassword = new OperatorPassword();
			newOperatorPassword.setOpCode(userName);
			newOperatorPassword.setPassword(newDigestedPassword);
			newOperatorPassword.setSalt(salt);
			service.updatePassword(newOperatorPassword);
		} catch (Exception e) {
			log.error("Error setting new password", e);
			if (e instanceof FormatException) {
				throw new LoginException("Password format exception: " + ArrayUtils.toString(((FormatException) e).getErrorList()));
			}
			throw new LoginException("Error setting new password: " + e.getMessage());
		}
	}

}
