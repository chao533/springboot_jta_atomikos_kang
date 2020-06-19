package com.kang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.kang.config.master.MasterDBConfig;
import com.kang.config.slave.SlaveDBConfig;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(value = {MasterDBConfig.class, SlaveDBConfig.class})
public class JtaAtomikosApplication {

	
	public static void main(String[] args) {
        SpringApplication.run(JtaAtomikosApplication.class, args);
    }
}
