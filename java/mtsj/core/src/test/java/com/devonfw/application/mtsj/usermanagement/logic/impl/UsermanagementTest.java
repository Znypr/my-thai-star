package com.devonfw.application.mtsj.usermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.ResetTokenEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo.ResetTokenRepository;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;

/**
 * TODO akkus This type ...
 *
 */
@SpringBootTest(classes = SpringBootApp.class)
public class UsermanagementTest extends ApplicationComponentTest {

  @Inject
  private Usermanagement userManagement;

  UserEto user;

  UserEto alreadySavedUser;

  @Inject
  private ResetTokenRepository resetDao;

  @Override
  protected void doSetUp() {

    super.doSetUp();

    this.user = new UserEto();

    this.user.setUsername("Tester11");
    this.user.setEmail("test11@test.com");
    this.user.setUserRoleId((long) 0);
    this.user.setPassword("password");

    UserEto user2 = new UserEto();

    user2.setId(123L);
    user2.setUsername("Tester10");
    user2.setEmail("test10@test.com");
    user2.setUserRoleId((long) 0);
    user2.setPassword("password2");
    this.alreadySavedUser = this.userManagement.saveUser(user2);

  }

  /**
   * Tests if an user can be created and deleted
   */
  @Test
  public void createUserAndDelete() {

    UserEto createdUser = this.userManagement.saveUser(this.user);
    assertThat(createdUser).isNotNull();
    Boolean isDeleted = this.userManagement.deleteUser(createdUser.getId());
    assertThat(isDeleted).isTrue();
    try {
      this.userManagement.findUser(createdUser.getId());
      fail("User shouldn't be accessible in database");
    } catch (Exception e) {
      assertThat(e.getClass()).isEqualTo(org.springframework.dao.EmptyResultDataAccessException.class);

    }

  }

  /**
   * Tests if the role of an user can be obtained from database
   */
  @Test
  public void checkUserrole() {

    UserEto user1 = this.userManagement.findUser(this.alreadySavedUser.getId());
    assertThat(this.userManagement.findUser(user1.getId()).getUserRoleId()).isEqualTo(0);
    assertThat(this.userManagement.findUserRole(this.userManagement.findUser(user1.getId()).getUserRoleId()).getName())
        .isEqualTo("Customer");

  }

  /**
   * Test if user gets a password-reset-email
   */
  @Test
  public void resetEmail() {

    Boolean result = this.userManagement.resetPassword(this.alreadySavedUser.getId());
    assertThat(result).isTrue();

  }

  /**
   * Test if user can change password over bookingToken
   */
  @Test
  public void changePassword() {

    ResetTokenEntity resetToken = this.resetDao.find((long) 0);
    UserEto user3 = this.userManagement.findUser((long) 0);

    UserEto transportUser = new UserEto();
    transportUser.setId(user3.getId());
    transportUser.setPassword("newpassword");
    transportUser.setToken(resetToken.getToken());

    UserEto user4 = this.userManagement.changePassword(transportUser);

    assertThat(this.userManagement.findUser(0L).getPassword()).isEqualTo(user4.getPassword());

  }

}
