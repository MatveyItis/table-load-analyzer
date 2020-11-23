package ru.maletskov.postgres.analyzer.config;

import java.util.HashMap;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "ru.maletskov.postgres.analyzer.repository.analyzer",
        entityManagerFactoryRef = "analyzerEntityManager",
        transactionManagerRef = "analyzerTransactionManager"
)
public class PersistenceAnalyzerConfig {

    @Autowired
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean analyzerEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(analyzerDataSource());
        em.setPackagesToScan("ru.maletskov.postgres.analyzer.entity.analyzer");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    public DataSource analyzerDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("analyzer.datasource.driver-class-name")));
        dataSource.setUrl(env.getProperty("analyzer.datasource.url"));
        dataSource.setUsername(env.getProperty("analyzer.datasource.username"));
        dataSource.setPassword(env.getProperty("analyzer.datasource.password"));
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager analyzerTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(analyzerEntityManager().getObject());
        return transactionManager;
    }
}
