package com.devonfw.application.mtsj.usermanagement.logic.api;

/**
 * TODO akkus This type ...
 *
 */
public interface ResetToken {

  /**
   * @param id of the user
   * @return true, if password reset worked
   */
  public boolean resetPassword(Long id);

  /**
   * @param token
   * @return id of the user
   */
  public ResetToken getResetTokenByToken(String token);
}
