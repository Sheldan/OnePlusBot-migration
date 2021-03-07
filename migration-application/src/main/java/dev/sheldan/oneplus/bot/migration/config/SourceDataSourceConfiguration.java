package dev.sheldan.oneplus.bot.migration.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "dev.sheldan.oneplus.bot.migration.starboard.repository",
        entityManagerFactoryRef = "sourceEntityManagerFactory",
        transactionManagerRef = "sourceTransactionManager"
)
public class SourceDataSourceConfiguration {

    @Bean
    @ConfigurationProperties("migration.datasource.source")
    public DataSourceProperties sourceDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("migration.datasource.source.configuration")
    public DataSource sourceDataSource() {
        return sourceDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Bean(name = "sourceEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sourceEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(sourceDataSource())
                .packages("dev.sheldan.oneplus.bot.migration")
                .build();
    }

    @Bean
    public PlatformTransactionManager sourceTransactionManager(
            final @Qualifier("sourceEntityManagerFactory") LocalContainerEntityManagerFactoryBean sourceEntityManagerFactory) {
        return new JpaTransactionManager(sourceEntityManagerFactory.getObject());
    }
}
