package eu.dorsum.webtrader.services;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import eu.dorsum.webtrader.domain.AbstractRq;
import eu.dorsum.webtrader.domain.IAuthorized;

/**
 * @author barnabas.peter
 * 
 *         Interceptor for MyBatis queries
 *
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class MyBatisQueryInterceptor implements Interceptor {

	private static final Logger logger = Logger.getLogger(MyBatisQueryInterceptor.class);

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		if (logger.isDebugEnabled()) {
			MappedStatement mp = (MappedStatement) invocation.getArgs()[0];
			logger.debug("Executing query:" + mp.getSqlSource().getBoundSql(invocation.getArgs()[1]).getSql());
		}

		long startTime = System.nanoTime();
		Object result = invocation.proceed();
		Object [] args=invocation.getArgs();
		long stopTime = System.nanoTime();

		if (logger.isDebugEnabled()) {
			logger.debug("Query duration: " + (stopTime - startTime) / 1000000 + "ms");
		}
		
		AbstractRq rq=null;
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof AbstractRq) {
				rq = (AbstractRq)args[i];
				break;
			}
		}
		if (result instanceof List) {
			Iterator<?> it = ((List<?>)result).iterator();
			while (it.hasNext()) {
				Object obj = it.next();
				if (obj instanceof IAuthorized && allowed(rq, obj) == false){
					it.remove();
				}
			}
		}
		return result;
	}

	private boolean allowed(AbstractRq rq, Object obj) {
		List<String> permissions = rq.getAuthorizationInfo().getPermissions();
		IAuthorized authorization = (IAuthorized)obj;
		String role = authorization.getRole();
		return permissions.contains(role);
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

}
