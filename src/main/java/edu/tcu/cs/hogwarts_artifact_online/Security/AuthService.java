package edu.tcu.cs.hogwarts_artifact_online.Security;

import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.Converter.UsertoUserDtoConverter;
import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.Dto.UserDto;
import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.HogwartsUser;
import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.MyUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;

    private final UsertoUserDtoConverter usertoUserDtoConverter;

    public AuthService(JwtProvider jwtProvider, UsertoUserDtoConverter usertoUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.usertoUserDtoConverter = usertoUserDtoConverter;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        MyUserPrincipal principal = (MyUserPrincipal)authentication.getPrincipal();
        HogwartsUser hogwartsUser = principal.getHogwartsUser();
        UserDto userDto = usertoUserDtoConverter.convert(hogwartsUser);

        String token = jwtProvider.createToken(authentication);
        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);
        return loginResultMap;
    }
}
