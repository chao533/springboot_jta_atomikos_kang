package com.kang.config.slave;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.kang.common.constant.MybatisConstants;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>Title: slaveMybatisConfig</p>
 * <p>Description: 数据源二<p>
 * @author ChaoKang
 * @date 2020年6月19日
 */
@Configuration
@MapperScan(basePackages = "com.kang.mapper.slave", sqlSessionTemplateRef = MybatisConstants.SLAVE_SQLSESSION_TEMPLATE)
public class SlaveMybatisConfig {
	
	@Bean(name = MybatisConstants.SLAVE_DATASOUCE)
	public DataSource slaveDataSource(SlaveDBConfig slaveDBConfig) throws SQLException {
		MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
		mysqlXaDataSource.setUrl(slaveDBConfig.getUrl());
		mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
		mysqlXaDataSource.setPassword(slaveDBConfig.getPassword());
		mysqlXaDataSource.setUser(slaveDBConfig.getUsername());
		mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
		// 将本地事务注册到创 Atomikos全局事务
		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		xaDataSource.setXaDataSource(mysqlXaDataSource);
		xaDataSource.setUniqueResourceName(MybatisConstants.SLAVE_DATASOUCE);

		xaDataSource.setMinPoolSize(slaveDBConfig.getMinPoolSize());
		xaDataSource.setMaxPoolSize(slaveDBConfig.getMaxPoolSize());
		xaDataSource.setMaxLifetime(slaveDBConfig.getMaxLifetime());
		xaDataSource.setBorrowConnectionTimeout(slaveDBConfig.getBorrowConnectionTimeout());
		xaDataSource.setLoginTimeout(slaveDBConfig.getLoginTimeout());
		xaDataSource.setMaintenanceInterval(slaveDBConfig.getMaintenanceInterval());
		xaDataSource.setMaxIdleTime(slaveDBConfig.getMaxIdleTime());
		xaDataSource.setTestQuery(slaveDBConfig.getTestQuery());
		return xaDataSource;
	}

	@Bean(name = MybatisConstants.SLAVE_SQLSESSION_FACTORY)
	public SqlSessionFactory slaveSqlSessionFactory(@Qualifier(MybatisConstants.SLAVE_DATASOUCE) DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/slave/*.xml"));

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class); // 日志输出
        sessionFactoryBean.setConfiguration(configuration);
        return sessionFactoryBean.getObject();
	}

	@Bean(name = MybatisConstants.SLAVE_SQLSESSION_TEMPLATE)
	public SqlSessionTemplate slaveSqlSessionTemplate(
			@Qualifier(MybatisConstants.SLAVE_SQLSESSION_FACTORY) SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}