package org.example.eartests.jwt.ejb;

import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import java.security.Principal;
import java.util.Set;

@Stateless
// FIXME: shoud not be required
@PermitAll
public class JwtEjbService {

    @Resource
    private SessionContext sessionContext;
    @Inject
    @Claim(standard = Claims.groups)
    private Set<String> jwtGroupsClaim;

    public String getCallerPricinpalName() {
        Principal callerPrincipal = sessionContext.getCallerPrincipal();
        String callerPrincipalClassName = callerPrincipal.getClass().getSimpleName();
        return callerPrincipal.getName() + " - " + callerPrincipalClassName;
    }

    public boolean isCallerAdmin() {
        boolean admin = sessionContext.isCallerInRole("admin");
        return admin;
    }

    public Set<String> getJwtGroupsClaim() {
        return Set.copyOf(jwtGroupsClaim);
    }

    public String getJaccSubjectName() {
        try {
            Subject subject = (Subject) PolicyContext.getContext("javax.security.auth.Subject.container");
            Set<? extends Principal> principalSet = subject.getPrincipals(JsonWebToken.class);
            assert principalSet.size() > 0;
            return principalSet.stream().findFirst()
                    .map(Principal::getName)
                    .orElse("No principal found");
        } catch (PolicyContextException e) {
            return e.getMessage();
        }
    }

    @RolesAllowed("admin")
    public boolean doSomeAdminStuff() {
        return true;
    }

}
