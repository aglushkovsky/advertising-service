package io.github.aglushkovsky.advertisingservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.aglushkovsky.advertisingservice.config.property.HibernateProperties;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

import static io.github.aglushkovsky.advertisingservice.config.property.HibernateProperties.*;

@Configuration
@AllArgsConstructor
@EnableTransactionManagement
public class JpaConfig {

    private HibernateProperties hibernateProperties;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(hibernateProperties.getDriver());
        hikariConfig.setJdbcUrl(hibernateProperties.getUrl());
        hikariConfig.setUsername(hibernateProperties.getUsername());
        hikariConfig.setPassword(hibernateProperties.getPassword());
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource());
        emfb.setPackagesToScan(hibernateProperties.getEntityPackage());
        emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfb.setJpaProperties(getHibernateProperties());
        return emfb;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty(PHYSICAL_NAMING_STRATEGY_KEY, hibernateProperties.getNamingPhysicalStrategy());
        properties.setProperty(DIALECT_KEY, hibernateProperties.getDialect());
        properties.setProperty(SHOW_SQL_KEY, hibernateProperties.getSql().getShow().toString());
        properties.setProperty(FORMAT_SQL_KEY, hibernateProperties.getSql().getFormat().toString());
        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(emf);
        return jpaTransactionManager;
    }
}
