package com.devonfw.application.mtsj.usermanagement.service.impl;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import com.devonfw.application.mtsj.general.common.api.constants.Roles;
import com.devonfw.application.mtsj.general.common.base.BaseUserDetails;
import com.devonfw.application.mtsj.general.service.impl.rest.SecurityRestServiceImpl;
import org.apache.http.impl.client.BasicAuthCache;
import org.springframework.data.domain.Page;

import com.devonfw.application.mtsj.usermanagement.common.api.to.UserEto;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserQrCodeTo;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserRoleEto;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserRoleSearchCriteriaTo;
import com.devonfw.application.mtsj.usermanagement.common.api.to.UserSearchCriteriaTo;
import com.devonfw.application.mtsj.usermanagement.logic.api.ResetToken;
import com.devonfw.application.mtsj.usermanagement.logic.api.Usermanagement;
import com.devonfw.application.mtsj.usermanagement.rest.api.UsermanagementRestService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.net.Authenticator;

/**
 * The service implementation for REST calls in order to execute the logic of component {@link Usermanagement}.
 */
@Named("UsermanagementRestService")
public class UsermanagementRestServiceImpl implements UsermanagementRestService {

    @Inject
    private Usermanagement usermanagement;

    @Inject
    private ResetToken resetToken;

    @Override
    public UserEto getUser(long id) {

        return this.usermanagement.findUser(id);
    }

    @Override
    public UserQrCodeTo getUserQrCode(String username) {

        return this.usermanagement.generateUserQrCode(username);
    }

    @Override
    public UserEto saveUser(UserEto user) {
        return this.usermanagement.saveUser(user);
    }

    @Override
    public UserEto getUserStatus(String username) {

        return this.usermanagement.getUserStatus(username);
    }

    @Override
    public UserEto saveUserTwoFactor(UserEto user) {

        return this.usermanagement.saveUserTwoFactor(user);
    }

    @Override
    public void deleteUser(long id) {
        this.usermanagement.deleteUser(id);
    }

    @Override
    public Page<UserEto> findUsersByPost(UserSearchCriteriaTo searchCriteriaTo) {

        return this.usermanagement.findUserEtos(searchCriteriaTo);
    }

    @Override
    public UserRoleEto getUserRole(long id) {

        return this.usermanagement.findUserRole(id);
    }

    @Override
    public UserRoleEto saveUserRole(UserRoleEto userrole) {

        return this.usermanagement.saveUserRole(userrole);
    }

    @Override
    public void deleteUserRole(long id) {

        this.usermanagement.deleteUserRole(id);
    }

    @Override
    public Page<UserRoleEto> findUserRolesByPost(UserRoleSearchCriteriaTo searchCriteriaTo) {

        return this.usermanagement.findUserRoleEtos(searchCriteriaTo);
    }

    @Override
    public void resetPassword(long id) {

        this.usermanagement.resetPassword(id);

    }

    @Override
    public Long getUserIdByToken(String token) {

        return this.resetToken.getResetTokenByToken(token);

    }

    @Override
    public UserEto changePassword(UserEto user) {

        return this.usermanagement.changePassword(user);

        // this.usermanagement.findUser(id).setPassword(null);

    }

    // @Override
    // public ResetTokenEto getIdUserByResetToken(String token) {
    //
    // return this.usermanagement.getIdUserByResetToken(token);
    //
    // }

}
