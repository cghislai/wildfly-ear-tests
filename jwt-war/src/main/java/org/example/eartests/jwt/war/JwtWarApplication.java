package org.example.eartests.jwt.war;

import org.eclipse.microprofile.auth.LoginConfig;

import javax.annotation.security.DeclareRoles;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
@LoginConfig(authMethod = "MP-JWT", realmName = "MP JWT Realm")
@DeclareRoles({"admin", "user"})
public class JwtWarApplication extends Application {
}
