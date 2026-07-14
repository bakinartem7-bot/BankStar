package com.starbank.recommendations.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.starbank.recommendations.repository",
        entityManagerFactoryRef = "ruleEntityManagerFactory",
        transactionManagerRef = "ruleTransactionManager"
)
public class DatabaseConfig {

    @Primary
    @Bean(name = "defaultDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "defaultJdbcTemplate")
    public JdbcTemplate defaultJdbcTemplate(
            @org.springframework.beans.factory.annotation.Qualifier("defaultDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "ruleDataSource")
    @ConfigurationProperties(prefix = "rule.datasource")
    public DataSource ruleDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "ruleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean ruleEntityManagerFactory(
            @org.springframework.beans.factory.annotation.Qualifier("ruleDataSource") DataSource ruleDataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ruleDataSource);
        em.setPackagesToScan("com.starbank.recommendations.model");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "true");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "ruleTransactionManager")
    public PlatformTransactionManager ruleTransactionManager(
            @org.springframework.beans.factory.annotation.Qualifier("ruleEntityManagerFactory")
            LocalContainerEntityManagerFactoryBean ruleEntityManagerFactory) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(ruleEntityManagerFactory.getObject());
        return transactionManager;
    }
}
