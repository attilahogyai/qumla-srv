package eu.dorsum.webtrader.services.login.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import eu.dorsum.webtrader.domain.login.OperatorPassword;
import eu.dorsum.webtrader.domain.login.PasswordExpirationData;
import eu.dorsum.webtrader.domain.login.PasswordSetting;
import eu.dorsum.webtrader.domain.user.UserLoginRq;

/**
 * Mybatis mapper to read/write datas by the login module.
 * The corresponding mapper xmls are in pb-impl or mak-impl
 */
public interface LoginModuleMapper {

	public OperatorPassword getOperatorPassword(@Param("opCode") String opCode);

	public void updatePassword(OperatorPassword newOperatorPassword);

	public void closeOldPassword(OperatorPassword operatorPassword);

	public List<PasswordSetting> getUasProperties();

	public void createSession(UserLoginRq request);

	public void closeSession(@Param("opCode") String opCode);

	public void updateFailedAttempts(@Param("opCode") String opCode, @Param("maxAllowed") int maxAllowed);

	public PasswordExpirationData getPasswordExpirationData(@Param("opCode") String opCode);

	public void updateOperatorOnLoginSuccess(UserLoginRq user);
	
	public void updatePasswordChangeDate(OperatorPassword user);
	
}
