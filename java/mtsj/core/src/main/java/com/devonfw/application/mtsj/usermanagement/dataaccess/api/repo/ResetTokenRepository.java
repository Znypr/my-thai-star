package com.devonfw.application.mtsj.usermanagement.dataaccess.api.repo;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devonfw.application.mtsj.usermanagement.common.api.to.ResetTokenSearchCriteriaTo;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.ResetTokenEntity;
import com.devonfw.application.mtsj.usermanagement.dataaccess.api.UserEntity;
import com.devonfw.module.jpa.dataaccess.api.QueryUtil;
import com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository;
import com.querydsl.core.alias.Alias;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * TODO akkus This type ...
 *
 */
public interface ResetTokenRepository extends DefaultRepository<ResetTokenEntity> {

  default Page<ResetTokenEntity> findResetToken(ResetTokenSearchCriteriaTo criteria) {

    ResetTokenEntity alias = newDslAlias();
    JPAQuery<ResetTokenEntity> query = newDslQuery(alias);

    String token = criteria.getToken();
    if ((token != null) && alias.getToken() != null) {
      query.where(Alias.$(alias.getToken()).eq(token));
    }
    Long idUser = criteria.getIdUser();
    if ((token != null) && alias.getIdUser() != null) {
      query.where(Alias.$(alias.getIdUser()).eq(idUser));
    }
    // boolean flag = criteria.isFlag();
    // if ((flag != false) && alias.isFlag() != false) {
    // query.where(Alias.$(alias.getToken()).eq(token));
    // }
    Date expires = criteria.getExpires();
    if ((expires != null) && alias.getExpires() != null) {
      query.where(Alias.$(alias.getExpires()).eq(expires));
    }

    return QueryUtil.get().findPaginated(criteria.getPageable(), query, false);
  }

  /**
   * @param token
   * @return An {@link UserEntity} objects that matched the search.
   */
  @Query("SELECT token FROM ResetTokenEntity token" //
      + " WHERE token.token = :token")
  ResetTokenEntity findByToken(@Param("token") String token);

}
