package testtask.testtaskforeffectivemobile.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testtask.testtaskforeffectivemobile.dto.AuthRequestDTO;
import testtask.testtaskforeffectivemobile.utils.JWTUtils;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@AllArgsConstructor
@Slf4j
public class AuthenticationController {
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String create(@RequestBody AuthRequestDTO authRequest) {
        var authentication = new UsernamePasswordAuthenticationToken(
            authRequest.getUsername(), authRequest.getPassword());

        authenticationManager.authenticate(authentication);

        var token = jwtUtils.generateToken(authRequest.getUsername());
        return token;
    }
}