package com.lcwd.electronic.store.ElectronicStore.controller;

import com.lcwd.electronic.store.ElectronicStore.dtos.UserDto;
import com.lcwd.electronic.store.ElectronicStore.dtos.jwt.JwtRequest;
import com.lcwd.electronic.store.ElectronicStore.dtos.jwt.JwtResponse;
import com.lcwd.electronic.store.ElectronicStore.entities.User;
import com.lcwd.electronic.store.ElectronicStore.security.JwtHelper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    //method to generate token:
    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        logger.info("UserName: {}, Password: {}", request.getEmail(),request.getPassword());

        this.doAuthenticate(request.getEmail(),request.getPassword());
        //generate token...
        //then send karna hai response

        User user = (User)userDetailsService.loadUserByUsername(request.getEmail());

        //generate token
        String token = jwtHelper.generateToken(user);

        JwtResponse jwtResponse = JwtResponse.builder()
                .jwtToken(token)
                .user(modelMapper.map(user, UserDto.class))
                .build();

        return ResponseEntity.ok(jwtResponse);
    }

    private void doAuthenticate(String email, String password){
        try{
            Authentication  authentication = new UsernamePasswordAuthenticationToken(email,password);
            authenticationManager.authenticate(authentication);

        }catch (BadCredentialsException ex){
            throw new BadCredentialsException("Invalid email & password !!");
        }
    }

}
