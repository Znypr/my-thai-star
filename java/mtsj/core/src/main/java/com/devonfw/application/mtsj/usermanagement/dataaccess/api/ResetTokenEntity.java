package com.devonfw.application.mtsj.usermanagement.dataaccess.api;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.devonfw.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import com.devonfw.application.mtsj.usermanagement.logic.api.ResetToken;

/**
 * TODO akkus This type ...
 *
 */

@Entity
@Table(name = "ResetToken")
public class ResetTokenEntity extends ApplicationPersistenceEntity implements ResetToken {

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
   * @return flag true, if used before
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

  @Override
  public boolean resetPassword(Long id) {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Long getResetTokenByToken(String token) {

    // TODO Auto-generated method stub
    return null;
  }

}
