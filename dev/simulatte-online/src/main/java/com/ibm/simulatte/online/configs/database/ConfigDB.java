package com.ibm.simulatte.online.configs.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

//@Configuration
//@PropertySource("classpath:application.properties")
//@EnableJpaRepositories
public class ConfigDB {

/*
    @Value("${spring.jpa.properties.hibernate.hbm2dll.create_namespaces}")
    private String hbmDllNamespaces;

    @Value("${spring.jpa.properties.hibernate.hbm2dll.auto}")
    private String hbmDllAuto;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hbDialect;

 */

    /*
    @Primary
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }*/

    /*
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(
                new String[] { "com.automation.simulatte.models" });

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2dll.create_namespaces", hbmDllNamespaces);
        properties.put("hibernate.hbm2ddl.auto", hbmDllAuto);
        properties.put("hibernate.dialect", hbDialect);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                entityManagerFactory().getObject());
        return transactionManager;
    }

*/
    /*
    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    @Primary
    public HikariDataSource dataSource() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(databaseUrl);
        config.setUsername(username);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    @Bean
    public Properties additionalProps() {
        Properties jpaProps = new Properties();

        jpaProps.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProps.put("hibernate.ddl-auto", "update");
        jpaProps.put("hibernate.show_sql", false);
        jpaProps.put("hibernate.format_sql", true);
        jpaProps.put("hibernate.hbm2dll.auto", "update");
        jpaProps.put("javax.persistence.create-database-schemas", true);
        jpaProps.put("hibernate.default_schema", "decision_execution");

        return jpaProps;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factory.setPackagesToScan("com.automation.simulatte.models");
        factory.setJpaProperties(additionalProps());
        return factory;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory factory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }*/
}
