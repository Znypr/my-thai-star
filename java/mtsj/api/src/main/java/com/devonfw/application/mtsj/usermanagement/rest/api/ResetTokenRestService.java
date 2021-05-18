package com.devonfw.application.mtsj.usermanagement.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.devonfw.application.mtsj.usermanagement.logic.api.ResetToken;

/**
 * The service interface for REST calls in order to execute the logic of component {@link ResetToken}.
 */
@Path("/usermanagement/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ResetTokenRestService {

  /**
   * @author akkus Delegates to {@link RestTokenImpl#resetPassword}.
   *
   * @param id ID of the {@link User} to be reset
   */
  @GET
  @Path("/resetPassword/{id}/")
  public void resetPassword(@PathParam("id") long id);

}
