package edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.Converter;

import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.Dto.UserDto;
import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.HogwartsUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UsertoUserDtoConverter implements Converter<HogwartsUser, UserDto> {
    @Override
    public UserDto convert(HogwartsUser source) {
        return new UserDto(source.getId(),source.getUsername(),source.isEnabled(),source.getRoles());
    }
}
