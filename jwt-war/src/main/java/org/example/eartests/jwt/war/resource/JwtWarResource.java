package org.example.eartests.jwt.war.resource;

import org.example.eartests.jwt.ejb.JwtEjbService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Optional;

@Path("/")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JwtWarResource {

    @Context
    private SecurityContext jaxRsSecurityContext;
    @Inject
    private JwtEjbService jwtEjbService;

    @GET
    @RolesAllowed("user")
    public JsonObject getJwtWar() {
        Principal userPrincipal = jaxRsSecurityContext.getUserPrincipal();
        String jaxRsPrincipalName = Optional.ofNullable(userPrincipal).map(Principal::getName).orElse(null);
        boolean jaxRsAdmin = jaxRsSecurityContext.isUserInRole("admin");

        JsonArray ejbServiceJwtGroupsArray = jwtEjbService.getJwtGroupsClaim()
                .stream()
                .reduce(Json.createArrayBuilder(), JsonArrayBuilder::add, JsonArrayBuilder::addAll)
                .build();

        String ejbServiceCallerPricinpalName = jwtEjbService.getCallerPricinpalName();
        boolean ejbServiceCallerAdmin = jwtEjbService.isCallerAdmin();
        String ejbServiceJaccSubjectName = jwtEjbService.getJaccSubjectName();
        return Json.createObjectBuilder()
                .add("jaxRsUser", jaxRsPrincipalName)
                .add("jaxRsAdmin", jaxRsAdmin)
                .add("ejbServiceUser", ejbServiceCallerPricinpalName)
                .add("ejbServiceAdmin", ejbServiceCallerAdmin)
                .add("ejbServiceJwtGroupsClaim", ejbServiceJwtGroupsArray)
                .add("ejbServiceJaccSubjectName", ejbServiceJaccSubjectName)
                .build();
    }

    @POST
    @RolesAllowed("admin")
    public JsonObject doSomeAdminStuff() {
        jwtEjbService.doSomeAdminStuff();
        return Json.createObjectBuilder()
                .add("adminStuff", "done")
                .build();
    }


}
