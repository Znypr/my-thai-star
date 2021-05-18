package com.devonfw.application.mtsj.usermanagement.common.api.to;

import java.util.Date;

import com.devonfw.application.mtsj.general.common.api.to.AbstractSearchCriteriaTo;

/**
 * TODO akkus This type ...
 *
 */
public class ResetTokenSearchCriteriaTo extends AbstractSearchCriteriaTo {

  private static final long serialVersionUID = 1L;

  private Long idUser;

  private Date expires;

  private String token;

  private boolean flag;

  /**
   * @return idUser
   */
  public Long getIdUser() {

    return this.idUser;
  }

  /**
   * @return expires
   */
  public Date getExpires() {

    return this.expires;
  }

  /**
   * @return flag
   */
  public boolean isFlag() {

    return this.flag;
  }

  /**
   * The constructor.
   */
  public ResetTokenSearchCriteriaTo() {

    super();
  }

  /**
   * @return serialversionuid
   */
  public static long getSerialversionuid() {

    return serialVersionUID;
  }

  /**
   * @return token
   */
  public String getToken() {

    return this.token;
  }

}
