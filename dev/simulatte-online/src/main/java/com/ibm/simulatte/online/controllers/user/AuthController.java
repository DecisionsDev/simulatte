package com.ibm.simulatte.online.controllers.user;

import com.ibm.simulatte.core.dto.UserDTO;
import com.ibm.simulatte.core.datamodels.user.AuthToken;
import com.ibm.simulatte.core.datamodels.user.User;
import com.ibm.simulatte.core.services.authentification.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@EnableAutoConfiguration
@RestController
@RequestMapping("v1/auth")
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;

    @Operation(
            summary = "Create user",
            description = "Create a application user with a role",
            tags = { "Authentication" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is created!"),
            @ApiResponse(responseCode = "404", description = "Fail to create new user") })
    @PostMapping("/signup")
    public User userSignUp(@RequestBody UserDTO newUserDTO) {
        User newUser = convertDtoToEntity(newUserDTO);
        return new User();
    }


    @Operation(
            summary = "Connect user",
            description = "Connect a application user with a role",
            tags = { "Authentication" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User is connected!"),
            @ApiResponse(responseCode = "404", description = "Fail to connect this user") })
    @PostMapping("/signin")
    public User userSignIn(@RequestBody UserDTO knownUserDTO) {
        User knownUser = convertDtoToEntity(knownUserDTO);
        return authService.signIn(knownUser);
    }

    @Operation(
            summary = "Refresh user token",
            description = "Refresh a app user token to stay connected (more security)",
            tags = { "Authentication" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token successful refresh!"),
            @ApiResponse(responseCode = "404", description = "Token refresh failed") })
    @PostMapping("/refreshToken")
    public AuthToken refreshToken(@RequestBody AuthToken token) throws UnsupportedEncodingException {
        return new AuthToken(); //authService.refreshToken(token);
    }

    private User convertDtoToEntity(UserDTO userDto)  {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT) ;
        return  modelMapper.map(userDto, User.class);
    }
}
