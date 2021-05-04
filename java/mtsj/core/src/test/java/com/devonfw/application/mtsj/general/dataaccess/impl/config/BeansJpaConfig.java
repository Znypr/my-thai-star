package com.devonfw.application.mtsj.general.dataaccess.impl.config;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;

import com.devonfw.application.mtsj.general.dataaccess.base.DatabaseMigrator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Java configuration for JPA
 */
@Configuration
public class BeansJpaConfig {

  @Inject
  private DataSource appDataSource;

  @Value("${database.migration.auto}")
  private boolean enabled;

    @Value("${database.migration.testdata}")
    private boolean testdata;

    @Value("${database.migration.clean}")
    private boolean clean;

  @Bean
  public DatabaseMigrator getFlyway() {

    DatabaseMigrator migrator = new DatabaseMigrator();
    migrator.setClean(this.clean);
    migrator.setDataSource(this.appDataSource);
    migrator.setEnabled(this.enabled);
    migrator.setTestdata(this.testdata);
    return migrator;

  }

  @PostConstruct
  public void migrate() {

    getFlyway().migrate();
  }

}