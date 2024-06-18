package edu.tcu.cs.hogwarts_artifact_online.HogwartsUser;

import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.Converter.UserDtoToUserConverter;
import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.Converter.UsertoUserDtoConverter;
import edu.tcu.cs.hogwarts_artifact_online.HogwartsUser.Dto.UserDto;
import edu.tcu.cs.hogwarts_artifact_online.System.Result;
import edu.tcu.cs.hogwarts_artifact_online.System.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    private final UsertoUserDtoConverter usertoUserDtoConverter;

    private final UserDtoToUserConverter userDtoToUserConverter;


    public UserController(UserService userService, UsertoUserDtoConverter usertoUserDtoConverter, UserDtoToUserConverter userDtoToUserConverter) {
        this.userService = userService;
        this.usertoUserDtoConverter = usertoUserDtoConverter;
        this.userDtoToUserConverter = userDtoToUserConverter;
    }
    @GetMapping
    public Result findAllUsers(){
        List<HogwartsUser> hogwartsUsers = userService.findAllUsers();
        List<UserDto> userDtos = hogwartsUsers.stream()
                .map(this.usertoUserDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS,"Found successfully",userDtos);
    }
    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Integer userId) {
        HogwartsUser hogwartsUser = this.userService.findUserById(userId);
        UserDto userDto = this.usertoUserDtoConverter.convert(hogwartsUser);
        return new Result(true,StatusCode.SUCCESS,"User found successfully", userDto);
    }
    @PostMapping
    public Result addUser(@Valid @RequestBody HogwartsUser hogwartsUser){
        HogwartsUser savedUser = this.userService.addUser(hogwartsUser);
        UserDto userDto = this.usertoUserDtoConverter.convert(savedUser);
        return new Result(true,StatusCode.SUCCESS,"User added successfully", userDto);
    }
    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto){
        HogwartsUser update = this.userDtoToUserConverter.convert(userDto);
        HogwartsUser updateHogwartsUser = this.userService.update(userId, update);
        UserDto updatedUserDto = this.usertoUserDtoConverter.convert(updateHogwartsUser);
        return new Result(true, StatusCode.SUCCESS, "User updated successfully", updatedUserDto);
    }
    @DeleteMapping("{userId}")
    public Result deleteUser(@PathVariable Integer userId){
        this.userService.delete(userId);
        return new Result(true,StatusCode.SUCCESS,"User deleted successfully");
    }
}
