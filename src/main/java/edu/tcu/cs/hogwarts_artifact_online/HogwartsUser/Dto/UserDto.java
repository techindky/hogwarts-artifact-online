package edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.Dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto (Integer id,
                       @NotEmpty(message = "username is required")
                       String username,
                       boolean enabled,
                       @NotEmpty(message = "roles are required")
                       String roles){
}
