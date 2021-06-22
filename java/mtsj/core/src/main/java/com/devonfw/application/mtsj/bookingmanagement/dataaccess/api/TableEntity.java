package com.devonfw.application.mtsj.bookingmanagement.dataaccess.api;

import javax.persistence.Entity;

import com.devonfw.application.mtsj.bookingmanagement.common.api.Table;
import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;

@Entity
@javax.persistence.Table(name = "\"Table\"")
public class TableEntity extends ApplicationPersistenceEntity implements Table {

  private Integer seatsNumber;
  private String tableName;
  private String alexaId;

  private static final long serialVersionUID = 1L;

  /**
   * @return seatsNumber
   */
  @Override
  public Integer getSeatsNumber() {

    return this.seatsNumber;
  }


  @Override
  public void setSeatsNumber(Integer seatsNumber) {

    this.seatsNumber = seatsNumber;
  }

  @Override
  public String getTableName() {
    return this.tableName;
  }

  @Override
  public String getAlexaID() {
    return this.alexaId;
  }

  @Override
  public void setTableName(String name) {
    this.tableName = name;
  }

  @Override
  public void setAlexaID(String id) {
    this.alexaId = id;
  }


}
