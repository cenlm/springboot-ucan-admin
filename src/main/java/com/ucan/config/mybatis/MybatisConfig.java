package com.ucan.config.mybatis;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.ucan.interceptors.PageInterceptor;

/**
 * @Description: mybatis配置类
 * @author liming.cen
 * @date 2023-03-21 18:19:40
 * 
 */
@Configuration
@MapperScan(basePackages="com.ucan.dao")
public class MybatisConfig {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ApplicationContext context;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
	SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
	factoryBean.setDataSource(dataSource);
	factoryBean.setMapperLocations(context.getResources("classpath:/mappers/*.xml"));
	return factoryBean.getObject();
    }

    /**
     * 分页拦截器
     * 
     * @return
     */
    @Bean
    public PageInterceptor pageInterceptor() {
	return new PageInterceptor();
    }
}
