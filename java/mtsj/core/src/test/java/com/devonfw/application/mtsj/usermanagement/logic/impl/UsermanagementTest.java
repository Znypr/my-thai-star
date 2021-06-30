package com.devonfw.application.mtsj.usermanagement.logic.impl;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.devonfw.application.mtsj.SpringBootApp;
import com.devonfw.application.mtsj.general.common.ApplicationComponentTest;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserQrCodeTo;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserRoleEto;
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

  @Inject
  private UsermanagementImpl userimpl;

  @Inject
  private ResetTokenRepository resetDao;

  @Override
  protected void doSetUp() {

    super.doSetUp();

  }

  /**
   * Tests if an user can be created and deleted
   */
  @Test
  public void createUserAndDelete() {

    UserEto user = new UserEto();

    user.setUsername("Tester1");
    user.setEmail("test1@test.com");
    user.setUserRoleId((long) 0);
    user.setPassword("password");

    UserEto createdUser = this.userManagement.saveUser(user);

    assertThat(createdUser).isNotNull();
    Boolean isDeleted = this.userManagement.deleteUser(createdUser.getId());
    assertThat(isDeleted).isTrue();
    try {
      this.userManagement.findUser(createdUser.getId());
      fail("User shouldn't be accessible in database");
    } catch (Exception e) {
      assertThat(e.getClass()).isEqualTo(org.springframework.dao.EmptyResultDataAccessException.class);
    }

    // try deleting with openOrders
    isDeleted = false;
    isDeleted = this.userManagement.deleteUser(0L);
    assertThat(!isDeleted);

  }

  /**
   * Tests if the role of an user can be obtained from database
   */
  @Test
  public void checkUserrole() {

    UserEto user2 = new UserEto();

    user2.setUsername("Tester2");
    user2.setEmail("test2@test.com");
    user2.setUserRoleId((long) 0);
    user2.setPassword("password");

    UserEto createdUser2 = this.userManagement.saveUser(user2);

    UserEto foundUser = this.userManagement.findUser(createdUser2.getId());
    assertThat(this.userManagement.findUser(foundUser.getId()).getUserRoleId()).isEqualTo(0);
    assertThat(
        this.userManagement.findUserRole(this.userManagement.findUser(foundUser.getId()).getUserRoleId()).getName())
            .isEqualTo("Customer");

  }

  /**
   * Test if user gets a password-reset-email
   */
  @Test
  public void resetEmail() {

    UserEto user3 = new UserEto();

    user3.setUsername("Tester3");
    user3.setEmail("test3@test.com");
    user3.setUserRoleId((long) 0);
    user3.setPassword("password");

    UserEto createdUser3 = this.userManagement.saveUser(user3);

    Boolean result = this.userManagement.resetPassword(createdUser3.getId());
    assertThat(result).isTrue();

  }

  /**
   * Test if user can change password over bookingToken
   */
  @Test
  public void changePassword() {

    ResetTokenEntity resetToken = this.resetDao.find((long) 0);
    UserEto user4 = this.userManagement.findUser((long) 0);

    UserEto transportUser = new UserEto();
    transportUser.setId(user4.getId());
    transportUser.setPassword("newpassword");
    transportUser.setToken(resetToken.getToken());

    UserEto foundUser2 = this.userManagement.changePassword(transportUser);

    assertThat(this.userManagement.findUser(0L).getPassword()).isEqualTo(foundUser2.getPassword());

  }

  /**
   * Test if user can change password over bookingToken
   */
  @Test
  public void findUserbyName() {

    UserEto user5 = new UserEto();

    user5.setUsername("Tester5");
    user5.setEmail("test5@test.com");
    user5.setUserRoleId((long) 0);
    user5.setPassword("password");

    UserEto createdUser5 = this.userManagement.saveUser(user5);

    UserEto foundUser3 = this.userManagement.findUserbyName(createdUser5.getUsername());
    assertThat(createdUser5.getId()).isEqualTo(foundUser3.getId());
  }

  /**
   * Test if user can generateQrCode
   */
  @Test
  public void generateUserQrCode() {

    UserEto user6 = new UserEto();

    user6.setUsername("Tester6");
    user6.setEmail("test6@test.com");
    user6.setUserRoleId((long) 0);
    user6.setPassword("password");
    user6.setTwoFactorStatus(true);

    UserEto createdUser6 = this.userManagement.saveUser(user6);

    UserQrCodeTo qrCode = this.userManagement.generateUserQrCode(createdUser6.getUsername());
    assertThat(qrCode).isNotNull();
  }

  // /**
  // * Test if user can generateQrCode
  // */
  // @Test
  // public void deleteUserRole() {
  //
  // UserEto user7 = new UserEto();
  //
  // user7.setUsername("Tester7");
  // user7.setEmail("test7@test.com");
  // user7.setUserRoleId((long) 0);
  // user7.setPassword("password");
  //
  // UserEto createdUser7 = this.userManagement.saveUser(user7);
  //
  // Boolean isDeleted = this.userManagement.deleteUserRole(createdUser7.getId());
  // assertThat(isDeleted);
  // }

  /**
   * Test if user can generateQrCode
   */
  @Test
  public void getResetTokenByToken() {

    ResetTokenEntity resetToken2 = this.resetDao.find((long) 0);
    System.err.println(resetToken2.getToken());
    Long id2 = this.userimpl.getResetTokenByToken(resetToken2.getToken());

    assertThat(0L).isEqualTo(id2);
  }

  /**
   * Test if user can generateQrCode
   */
  @Test
  public void saveUserTwoFactor() {

    UserEto user8 = new UserEto();

    user8.setUsername("Tester8");
    user8.setEmail("test8@test.com");
    user8.setUserRoleId((long) 0);
    user8.setPassword("password");
    user8.setTwoFactorStatus(false);

    UserEto createdUser8 = this.userManagement.saveUser(user8);
    createdUser8.setTwoFactorStatus(true);
    UserEto changedUser8 = this.userManagement.saveUserTwoFactor(createdUser8);
    assertThat(true).isEqualTo(changedUser8.getTwoFactorStatus());
  }

  /**
   *
   */
  @Test
  public void findUserRole() {

    UserRoleEto userrole = this.userManagement.findUserRole(0L);
    assertThat("Customer").isEqualTo(userrole.getName());
  }

  /**
  *
  */
  @Test
  public void saveUserRoleAndDeleteIt() {

    UserRoleEto newRole = new UserRoleEto();
    newRole.setName("Cook");
    UserRoleEto userrole2 = this.userManagement.saveUserRole(newRole);
    assertThat("Cook").isEqualTo(userrole2.getName());

    Boolean isDeleted = this.userManagement.deleteUserRole(userrole2.getId());
    assertThat(isDeleted);
  }

}
