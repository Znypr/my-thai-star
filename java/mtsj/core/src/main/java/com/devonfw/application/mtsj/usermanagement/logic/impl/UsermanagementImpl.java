package com.devonfw.application.mtsj.usermanagement.logic.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.jboss.aerogear.security.otp.api.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.devonfw.application.mtsj.general.common.api.UserProfile;
import com.devonfw.application.mtsj.general.common.api.datatype.Role;
import com.devonfw.application.mtsj.general.common.api.to.UserDetailsClientTo;
import com.devonfw.application.mtsj.general.common.base.QrCodeService;
import com.devonfw.application.mtsj.general.logic.base.AbstractComponentFacade;
import com.devonfw.application.mtsj.general.service.impl.config.WebSecurityBeansConfig;
import com.devonfw.application.mtsj.mailservice.logic.api.Mail;
import com.devonfw.application.mtsj.usermanagement.common.api.to.ResetTokenEto;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserQrCodeTo;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserRoleEto;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserRoleSearchCriteriaTo;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserSearchCriteriaTo;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.ResetTokenEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.UserRoleEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.ResetTokenRepository;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.UserRepository;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.UserRoleRepository;
import com.devonfw.application.mtsj.usermanagement.logic.api.ResetToken;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;

/**
 * Implementation of component interface of usermanagement
 */
@Named
@Transactional
public class UsermanagementImpl extends AbstractComponentFacade implements Usermanagement, ResetToken {

  private static final Logger LOG = LoggerFactory.getLogger(UsermanagementImpl.class);

  @Inject
  private UserRepository userDao;

  @Inject
  private UserRoleRepository userRoleDao;

  @Inject
  private ResetTokenRepository resetDao;

  @Inject
  private Mail mailService;

  @Value("${client.port}")
  private int clientPort;

  @Value("${server.servlet.context-path}")
  private String serverContextPath;

  @Value("${mythaistar.hourslimitcancellation}")
  private int hoursLimit;

  /**
   * The constructor.
   */
  public UsermanagementImpl() {

    super();
  }

  @Override
  public ResetTokenEto getResetTokenByToken(String token) {

    LOG.debug("Get User with token {} from database.", token);
    LOG.error("ResetDAO: " + getResetDao().find((long) 0).toString());
    return getBeanMapper().map(getResetDao().find((long) 0), ResetTokenEto.class);
    // ResetTokenEto tokenTo = getBeanMapper().map(getResetDao().find((long) 0), ResetTokenEto.class);
    // LOG.error("tokenTo: " + tokenTo.getIdUser() + "/n " + tokenTo.getToken());
    // return tokenTo;
  }

  private String buildToken(String email) throws NoSuchAlgorithmException {

    Instant now = Instant.now();
    LocalDateTime ldt1 = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
    String date = String.format("%04d", ldt1.getYear()) + String.format("%02d", ldt1.getMonthValue())
        + String.format("%02d", ldt1.getDayOfMonth()) + "_";

    String time = String.format("%02d", ldt1.getHour()) + String.format("%02d", ldt1.getMinute())
        + String.format("%02d", ldt1.getSecond());

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update((email + date + time).getBytes());
    byte[] digest = md.digest();
    StringBuilder sb = new StringBuilder();
    for (byte b : digest) {
      sb.append(String.format("%02x", b & 0xff));
    }
    return "RT_" + date + sb;
  }

  /**
   * @param id of the user
   * @return true, if password reset worked
   */
  @Override
  public boolean resetPassword(Long id) {

    UserEntity user = getUserDao().find(id);
    String token;
    try {

      token = buildToken(user.getEmail());
      sendResetEmailToUser(user, token);

      ResetTokenEntity resetTokenEntity = new ResetTokenEntity();
      resetTokenEntity.setExpires(new Date());
      resetTokenEntity.setFlag(false);
      resetTokenEntity.setIdUser(id);
      resetTokenEntity.setToken(token);
      getResetDao().save(resetTokenEntity);

    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
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
      resetMailContent.append("Hi ").append(user.getEmail()).append("\n");
      resetMailContent.append("You have requested a password reset");

      // TODO akkus <RestPath needs to be implemented and added here>
      String linkReset = getClientUrl() + "/resetPassword/?token=" + token;

      resetMailContent.append("To reset: ").append(linkReset).append("\n");

      LOG.error("Die Emailadresse lautet: " + user.getEmail());
      this.mailService.sendMail(user.getEmail(), "Password Reset", resetMailContent.toString());
    } catch (Exception e) {
      LOG.error("Email not sent. {}", e.getMessage());
    }

  }

  @Override
  public UserEto findUser(Long id) {

    LOG.debug("Get User with id {} from database.", id);
    return getBeanMapper().map(getUserDao().find(id), UserEto.class);
  }

  @Override
  public UserQrCodeTo generateUserQrCode(String username) {

    LOG.debug("Get User with username {} from database.", username);
    UserEntity user = getBeanMapper().map(getUserDao().findByUsername(username), UserEntity.class);
    initializeSecret(user);
    if (user != null && user.getTwoFactorStatus()) {
      return QrCodeService.generateQrCode(user);
    } else {
      return null;
    }
  }

  @Override
  public UserEto getUserStatus(String username) {

    LOG.debug("Get User with username {} from database.", username);
    return getBeanMapper().map(getUserDao().findByUsername(username), UserEto.class);
  }

  @Override
  public UserEto findUserbyName(String userName) {

    UserEntity entity = this.userDao.findByUsername(userName);
    return getBeanMapper().map(entity, UserEto.class);
  }

  @Override
  public Page<UserEto> findUserEtos(UserSearchCriteriaTo criteria) {

    Page<UserEntity> users = getUserDao().findUsers(criteria);
    return mapPaginatedEntityList(users, UserEto.class);
  }

  @Override
  public boolean deleteUser(Long userId) {

    UserEntity user = getUserDao().find(userId);
    getUserDao().delete(user);
    LOG.debug("The user with id '{}' has been deleted.", userId);
    return true;
  }

  @Override
  public UserEto saveUser(UserEto user) {

    Objects.requireNonNull(user, "user");
    WebSecurityBeansConfig webSecurityBeansConfig = new WebSecurityBeansConfig();
    user.setPassword(webSecurityBeansConfig.passwordEncoder().encode(user.getPassword()));

    UserEntity userEntity = getBeanMapper().map(user, UserEntity.class);

    // initialize, validate userEntity here if necessary
    UserEntity resultEntity = getUserDao().save(userEntity);
    // debugging
    LOG.debug("User with id '{}' has been created.", resultEntity.getId());
    return getBeanMapper().map(resultEntity, UserEto.class);
  }

  @Override
  public UserEto saveUserTwoFactor(UserEto user) {

    Objects.requireNonNull(user, "user");
    UserEntity userEntity = getBeanMapper().map(getUserDao().findByUsername(user.getUsername()), UserEntity.class);

    // initialize, validate userEntity here if necessary
    userEntity.setTwoFactorStatus(user.getTwoFactorStatus());
    UserEntity resultEntity = getUserDao().save(userEntity);
    LOG.debug("User with id '{}' has been modified.", resultEntity.getId());
    return getBeanMapper().map(resultEntity, UserEto.class);
  }

  /**
   * Returns the field 'userDao'.
   *
   * @return the {@link UserRepository} instance.
   */
  public UserRepository getUserDao() {

    return this.userDao;
  }

  /**
   * @return
   */
  public ResetTokenRepository getResetDao() {

    return this.resetDao;
  }

  @Override
  public UserRoleEto findUserRole(Long id) {

    LOG.debug("Get UserRole with id {} from database.", id);
    return getBeanMapper().map(getUserRoleDao().find(id), UserRoleEto.class);
  }

  @Override
  public Page<UserRoleEto> findUserRoleEtos(UserRoleSearchCriteriaTo criteria) {

    Page<UserRoleEntity> userroles = getUserRoleDao().findUserRoles(criteria);
    return mapPaginatedEntityList(userroles, UserRoleEto.class);
  }

  @Override
  public boolean deleteUserRole(Long userRoleId) {

    UserRoleEntity userRole = getUserRoleDao().find(userRoleId);
    getUserRoleDao().delete(userRole);
    LOG.debug("The userRole with id '{}' has been deleted.", userRoleId);
    return true;
  }

  @Override
  public UserRoleEto saveUserRole(UserRoleEto userRole) {

    Objects.requireNonNull(userRole, "userRole");
    UserRoleEntity userRoleEntity = getBeanMapper().map(userRole, UserRoleEntity.class);

    // initialize, validate userRoleEntity here if necessary
    UserRoleEntity resultEntity = getUserRoleDao().save(userRoleEntity);
    LOG.debug("UserRole with id '{}' has been created.", resultEntity.getId());

    return getBeanMapper().map(resultEntity, UserRoleEto.class);
  }

  /**
   * Assigns a randomly generated secret for an specific user
   *
   * @param user
   */
  private void initializeSecret(UserEntity user) {

    if (user.getSecret() == null) {
      user.setSecret(Base32.random());
      UserEntity resultEntity = getUserDao().save(user);
      LOG.debug("User with id '{}' has been modified.", resultEntity.getId());
    }
  }

  /**
   * Returns the field 'userRoleDao'.
   *
   * @return the {@link UserRoleRepository} instance.
   */
  public UserRoleRepository getUserRoleDao() {

    return this.userRoleDao;
  }

  @Override
  public UserProfile findUserProfileByLogin(String login) {

    UserEto userEto = findUserbyName(login);
    UserDetailsClientTo profile = new UserDetailsClientTo();
    profile.setId(userEto.getId());
    profile.setRole(Role.getRoleById(userEto.getUserRoleId()));
    return profile;
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
  public Long getIdUserByResetToken(String token) {

    // getResetDao().findAll();
    return null;
  }

}
