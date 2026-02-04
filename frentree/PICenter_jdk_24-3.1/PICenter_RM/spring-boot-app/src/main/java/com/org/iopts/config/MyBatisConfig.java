package com.org.iopts.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * MyBatis Configuration
 */
@Configuration
@MapperScan(basePackages = {
    "com.org.iopts.dao",
    "com.org.iopts.mapper",
    "com.org.iopts.popup.dao",
    "com.org.iopts.detection.dao",
    "com.org.iopts.group.dao",
    "com.org.iopts.exception.dao",
    "com.org.iopts.report.dao",
    "com.org.iopts.mockup.dao",
    "com.org.iopts.search.dao",
    "com.org.iopts.download.dao",
    "com.org.iopts.mail.dao",
    "com.org.iopts.otp.dao",
    "com.org.iopts.setting.dao"
})
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setConfigLocation(
            new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml")
        );
        sessionFactory.setMapperLocations(
            new PathMatchingResourcePatternResolver().getResources("classpath:mappers/**/*.xml")
        );
        sessionFactory.setTypeAliasesPackage("com.org.iopts.dto");
        return sessionFactory.getObject();
    }

    @Bean
    public SqlSession sqlSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
