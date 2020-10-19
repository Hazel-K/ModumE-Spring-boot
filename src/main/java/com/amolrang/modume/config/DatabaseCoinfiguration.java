package com.amolrang.modume.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySource("classpath:/application.properties")
public class DatabaseCoinfiguration {
	
	// 스프링 IoC Container(AppConfig 처럼 객체를 생성하고 관리하면서 의존관계를 연결해주는 것)를 사용하기 위한 applicationContext 주입
	@Autowired
	private ApplicationContext applicationContext;
	
	// DataSource 객체를 받아서 만들어진 setDataSource로 설정 값, 
	// setMapperLocations로 mapper 파일 스캔 경로 등 기본을 설정 한 sqlSessionFactory빈이다.
	// “classpath:/mapper/*/.xml”는 예를 들어서 mapper 경로를 src/main/resources/mapper 밑으로 
	// /example/example.xml 파일들를 스캔해서 설정하겠다는 의미이다.
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		// classpath는 자바 클래스의 resources에 접근하는 변수 ~~?~??~~?~??~!~!~~! 찾아보기1@~!~!!~!@~!@@!#!@#!@
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mapper/**/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}
	
	// MyBatis에서 SqlSession를 이용해 DataSource(데이터베이스 연결정보)로 실제로 DB에 접근하는 빈
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}
	
	@Bean
	public DataSource dataSource() {
		DataSource dataSource = new HikariDataSource(hikariConfig());
		log.info("datasource : {}", dataSource);
		return dataSource;
	}
}
