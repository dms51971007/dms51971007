package com.signalm.manager.config;

import java.beans.PropertyVetoException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@ComponentScan({"com.signalm.manager.config"})
@ComponentScan(basePackages = "com.signalm.manager")
@PropertySource("classpath:persistence-mysql.properties")
@EnableTransactionManagement
public class SignalManagerConfig implements WebMvcConfigurer {

    private final Environment env;

    private final Logger logger = Logger.getLogger(getClass().getName());

    public SignalManagerConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.signalm.manager.model", "com.signalm.manager.serv");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @org.jetbrains.annotations.NotNull
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.jdbc.time_zone", "Europe/Moscow");
        properties.put(AvailableSettings.HBM2DDL_HALT_ON_ERROR, true);
        properties.put("hibernate.show_sql", true);
        return properties;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }
    @Bean
    public ViewResolver viewResolver() {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/resource/**").addResourceLocations("/WEB-INF/resource/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());

    }


     @Bean
    public DataSource dataSource(){

        ComboPooledDataSource dataSource
                = new ComboPooledDataSource();


        try {
            dataSource.setDriverClass(env.getProperty("jdbc.driver"));
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        logger.info(">>> jdbc.url=" + env.getProperty("jdbc.url"));
        logger.info(">>> jdbc.user=" + env.getProperty("jdbc.user"));



        dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        dataSource.setUser(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.password"));

        dataSource.setInitialPoolSize(
                getIntProperty("connection.pool.initialPoolSize"));

        dataSource.setMinPoolSize(
                getIntProperty("connection.pool.minPoolSize"));

        dataSource.setMaxPoolSize(
                getIntProperty("connection.pool.maxPoolSize"));
         dataSource.setTestConnectionOnCheckout( true );
         dataSource.setPreferredTestQuery( "SELECT 1" );

        dataSource.setMaxIdleTime(
                getIntProperty("connection.pool.maxIdleTime"));
         try {
             dataSource.getLastConnectionTestFailureDefaultUser();
         } catch (SQLException throwables) {
             throwables.printStackTrace();
         }
         return dataSource;
    }


    private int getIntProperty(String propName) {
        return Integer.parseInt(Objects.requireNonNull(env.getProperty(propName)));
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    @Bean
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setDefaultEncoding("utf-8");
        commonsMultipartResolver.setMaxUploadSize(20000000);
        commonsMultipartResolver.setResolveLazily(false);
        return commonsMultipartResolver;
    }

}
















