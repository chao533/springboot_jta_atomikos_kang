package com.kang.config.master;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.kang.common.constant.MybatisConstants;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>Title: MasterMybatisConfig</p>
 * <p>Description: 数据源一<p>
 * @author ChaoKang
 * @date 2020年6月19日
 */
@Configuration
@MapperScan(basePackages = "com.kang.mapper.master", sqlSessionTemplateRef = MybatisConstants.MASTER_SQLSESSION_TEMPLATE)
public class MasterMybatisConfig {
	
	@Bean(name = MybatisConstants.MASTER_DATASOUCE)
	@Primary
	public DataSource masterDataSource(MasterDBConfig masterDBConfig) throws SQLException {
		MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
		mysqlXaDataSource.setUrl(masterDBConfig.getUrl());
		mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
		mysqlXaDataSource.setPassword(masterDBConfig.getPassword());
		mysqlXaDataSource.setUser(masterDBConfig.getUsername());
		mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
		// 将本地事务注册到创 Atomikos全局事务
		AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
		xaDataSource.setXaDataSource(mysqlXaDataSource);
		xaDataSource.setUniqueResourceName(MybatisConstants.MASTER_DATASOUCE);

		xaDataSource.setMinPoolSize(masterDBConfig.getMinPoolSize());
		xaDataSource.setMaxPoolSize(masterDBConfig.getMaxPoolSize());
		xaDataSource.setMaxLifetime(masterDBConfig.getMaxLifetime());
		xaDataSource.setBorrowConnectionTimeout(masterDBConfig.getBorrowConnectionTimeout());
		xaDataSource.setLoginTimeout(masterDBConfig.getLoginTimeout());
		xaDataSource.setMaintenanceInterval(masterDBConfig.getMaintenanceInterval());
		xaDataSource.setMaxIdleTime(masterDBConfig.getMaxIdleTime());
		xaDataSource.setTestQuery(masterDBConfig.getTestQuery());
		return xaDataSource;
	}

	@Bean(name = MybatisConstants.MASTER_SQLSESSION_FACTORY)
	public SqlSessionFactory masterSqlSessionFactory(@Qualifier(MybatisConstants.MASTER_DATASOUCE) DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/master/*.xml"));

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class); // 日志输出
        sessionFactoryBean.setConfiguration(configuration);
        return sessionFactoryBean.getObject();
	}

	@Bean(name = MybatisConstants.MASTER_SQLSESSION_TEMPLATE)
	public SqlSessionTemplate masterSqlSessionTemplate(
			@Qualifier(MybatisConstants.MASTER_SQLSESSION_FACTORY) SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}