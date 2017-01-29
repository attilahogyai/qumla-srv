package eu.dorsum.webtrader.services.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.Timeout;

/**
 * Abstract class for all mybatis mapper tests.
 * Opens a new connection to the database and rollback after the test has run.
 */
public class AbstractMyBatisMapperTest<T> extends ExternalResource {

	private static final Logger LOGGER = Logger.getLogger(AbstractMyBatisMapperTest.class);
	private static Map<TestDb, SqlSessionFactory> sessionFactories = new HashMap<>();
	private static boolean initialized;
	
	protected enum TestDb {
		Oracle, MsSQL
	}

	@Rule
    public Timeout globalTimeout = Timeout.seconds(5);
	
	protected Map<TestDb, T> mappers = new HashMap<>();
	private Map<TestDb, SqlSession> sessions = new HashMap<>();
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		if (initialized) {
			return;
		}
		LOGGER.info("Initialize database connections...");
		for (TestDb db : TestDb.values()) {
			Configuration config = getConfiguration();
			DataSource dataSource = getDataSource(db);
			SqlSessionFactory sessionFactory = createSessionFactory(db.toString(), config, dataSource);
			boolean connectionOk = isConnectionOK(dataSource);
			if (connectionOk) {
				sessionFactories.put(db, sessionFactory);
				LOGGER.info(db.toString() + " db connection OK");
			} else {
				LOGGER.info(db.toString() + " database connection not avaible, skipping related tests.");
			}
		}
		initialized = true;
	}

	private static Configuration getConfiguration() throws IOException {
		try (Reader reader = Resources.getResourceAsReader("META-INF/mybatis-config.xml")) {
			XMLConfigBuilder parser = new XMLConfigBuilder(reader, null, null);
			Configuration config = parser.parse();
			config.addMappers("eu.dorsum.webtrader.services");
			config.setCacheEnabled(false);
			return config;
		}
	}

	private static SqlSessionFactory createSessionFactory(String name, Configuration config, DataSource dataSource) {
		TransactionFactory transactionFactory = new JdbcTransactionFactory();
		Environment environment = new Environment(name, transactionFactory, dataSource);
		config.setEnvironment(environment);
		return new SqlSessionFactoryBuilder().build(config);
	}

	private static DataSource getDataSource(TestDb db) throws SQLException {
		DataSource dataSource = null;
		switch (db) {
			case Oracle:
				dataSource = new PooledDataSource("oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@10.5.1.102:1522:clvprj", "web_trader", "12345678");
				break;
			case MsSQL:
				dataSource = new PooledDataSource("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://192.168.9.24;instanceName=mssql2008r2;integratedSecurity=false;databaseName=pb2_new_dev", "sa", "12345678");
				break;
			default:
				throw new IllegalArgumentException("Unknown test db: " + db);
		}
		dataSource.setLoginTimeout(1);
		return dataSource;
	}
	
	@SuppressWarnings("resource")
	public void initDb(Class<T> mapperClass, TestDb... dbs) {
		for (TestDb db : dbs) {
			SqlSessionFactory factory = sessionFactories.get(db);
			if (factory == null) {
				continue;
			}
			SqlSession session = factory.openSession(false);
			sessions.put(db, session);
		}
		//If none of the database connections are avaiable, we skip the test
		Assume.assumeFalse(sessions.isEmpty());
		generateMappers(mapperClass);
	}

	@SuppressWarnings("resource")
	private void generateMappers(Class<T> mapperClass) {
		for (Entry<TestDb, SqlSession> sessionEntry : sessions.entrySet()) {
			TestDb key = sessionEntry.getKey();
			LOGGER.debug("Creating mapper for: " + key + ", " + mapperClass.getSimpleName());
			SqlSession session = sessionEntry.getValue();
			T mapper = session.getMapper(mapperClass);
			assertNotNull(mapper);
			mappers.put(key, mapper);
		}
	}

	@After
	public void after() {
		for (SqlSession session : sessions.values()) {
			session.rollback();
			session.close();
		}
	}
	
	private static boolean isConnectionOK(DataSource datasource) {
		try (Connection connection = datasource.getConnection();
			 Statement statement = connection.createStatement()) {
			statement.executeQuery("select 1 from dual");
			return true;
		} catch (Exception e) {	}
		return false;
	}

}
