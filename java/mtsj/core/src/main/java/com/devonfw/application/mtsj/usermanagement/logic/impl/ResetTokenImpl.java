package com.devonfw.application.mtsj.usermanagement.logic.impl;

import java.nio.charset.Charset;
import java.util.Random;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.devonfw.application.mtsj.mailservice.logic.api.Mail;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.UserRepository;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.UserRoleRepository;
import com.devonfw.application.mtsj.usermanagement.logic.api.ResetToken;

/**
 * TODO akkus This type ...
 *
 */
public class ResetTokenImpl implements ResetToken {

  private static final Logger LOG = LoggerFactory.getLogger(ResetTokenImpl.class);

  @Inject
  private UserRepository userDao;

  @Inject
  private UserRoleRepository userRoleDao;

  @Inject
  private Mail mailService;

  @Value("${client.port}")
  private int clientPort;

  @Value("${server.servlet.context-path}")
  private String serverContextPath;

  @Value("${mythaistar.hourslimitcancellation}")
  private int hoursLimit;

  private String buildToken() {

    byte[] array = new byte[64]; // length is bounded by 7
    new Random().nextBytes(array);
    String generatedString = new String(array, Charset.forName("UTF-8"));

    return generatedString;
  }

  /**
   * @param id of the user
   * @return true, if password reset worked
   */
  @Override
  public boolean resetPassword(Long id) {

    String token = buildToken();

    // UserEntity user = getUserDao().find(id);
    // sendResetEmailToUser(user, token);
    return true;
  }

  /**
   * Send a reset email to an user
   *
   * @param user identifies the user
   */
  private void sendResetEmailToUser(UserEntity user, String token) {

    try {
      StringBuilder resetMailContent = new StringBuilder();
      resetMailContent.append("MY THAI STAR").append("\n");
      resetMailContent.append("Hi ").append(user.getUsername()).append("\n");
      resetMailContent.append("You have requested a password reset");

      // TODO akkus <RestPath needs to be implemented and added here>
      String linkReset = getClientUrl() + "/resetPassword/" + token;

      resetMailContent.append("To reset: ").append(linkReset).append("\n");

      LOG.error("Die Emailadresse lautet: " + user.getEmail());
      this.mailService.sendMail(user.getEmail(), "Password Reset", resetMailContent.toString());
    } catch (Exception e) {
      LOG.error("Email not sent. {}", e.getMessage());
    }

  }

  private String getClientUrl() {

    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    String clientUrl = request.getHeader("origin");
    if (clientUrl == null) {
      return "http://localhost:" + this.clientPort;
    }
    return clientUrl;
  }

  @Override
  public Long getResetTokenByToken(String token) {

    // TODO Auto-generated method stub
    return null;
  }

}
