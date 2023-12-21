package com.scand.bookshop.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

  @Bean
  public DataSource dataSource() {
    return DataSourceBuilder.create()
        .url("jdbc:postgresql://localhost:5432/quartz")
        .username("postgres")
        .password("root")
        .build();
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
    SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
    schedulerFactory.setSchedulerName("payment-scheduler");
    schedulerFactory.setQuartzProperties(getQuartzProperties());
    schedulerFactory
        .setDataSource(dataSource);
    schedulerFactory.setApplicationContextSchedulerContextKey("applicationContext");
    return schedulerFactory;
  }

  private Properties getQuartzProperties() {
    Properties properties = new Properties();
    properties.setProperty(StdSchedulerFactory.PROP_JOB_STORE_CLASS,
        LocalDataSourceJobStore.class.getName());
    properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
    properties.setProperty("org.quartz.jobStore.class", LocalDataSourceJobStore.class.getName());
    properties
        .setProperty("org.quartz.jobStore.driverDelegateClass",
            "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
    properties.setProperty("org.quartz.jobStore.isClustered", "true");
    return properties;
  }
}