package com.devonfw.application.mtsj.usermanagement.common.api.to;

import java.util.Date;

import com.devonfw.module.basic.common.api.to.AbstractEto;

/**
 * TODO akkus This type ...
 *
 */
public class ResetTokenEto extends AbstractEto {
  private Long idUser;

  /**
   * @return idUser
   */
  public Long getIdUser() {

    return this.idUser;
  }

  /**
   * @param idUser new value of {@link #getidUser}.
   */
  public void setIdUser(Long idUser) {

    this.idUser = idUser;
  }

  /**
   * @return expires
   */
  public Date getExpires() {

    return this.expires;
  }

  /**
   * @param expires new value of {@link #getexpires}.
   */
  public void setExpires(Date expires) {

    this.expires = expires;
  }

  /**
   * @return token
   */
  public String getToken() {

    return this.token;
  }

  /**
   * @param token new value of {@link #gettoken}.
   */
  public void setToken(String token) {

    this.token = token;
  }

  /**
   * @return flag
   */
  public boolean isFlag() {

    return this.flag;
  }

  /**
   * @param flag new value of {@link #getflag}.
   */
  public void setFlag(boolean flag) {

    this.flag = flag;
  }

  private Date expires;

  private String token;

  private boolean flag;

}
