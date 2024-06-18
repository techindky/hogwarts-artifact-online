package edu.tcu.cs.hogwarts_artifact_online.Security;

import edu.tcu.cs.hogwarts_artifact_online.System.Result;
import edu.tcu.cs.hogwarts_artifact_online.System.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class AuthController {
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    public Result getLoginInfo(Authentication authentication){
        logger.debug("Authenticated user: '{}'", authentication.getName());
        return new Result(true, StatusCode.SUCCESS,"User Info and JSON web token", authService.createLoginInfo(authentication));
    }
}
