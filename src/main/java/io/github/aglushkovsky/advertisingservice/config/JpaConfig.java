package io.github.aglushkovsky.advertisingservice.config;

import io.github.aglushkovsky.advertisingservice.config.property.HibernateProperties;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
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
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(hibernateProperties.getDriver());
        driverManagerDataSource.setUrl(hibernateProperties.getUrl());
        driverManagerDataSource.setUsername(hibernateProperties.getUsername());
        driverManagerDataSource.setPassword(hibernateProperties.getPassword());
        return driverManagerDataSource;
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
