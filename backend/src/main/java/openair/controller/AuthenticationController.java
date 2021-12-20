package openair.controller;

import openair.dto.LoginDTO;
import openair.dto.UserTokenState;
import openair.model.User;
import openair.dto.JwtAuthenticationRequest;
import openair.service.UserService;
import openair.utils.TokenUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private TokenUtils tokenUtils;
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public AuthenticationController(TokenUtils tokenUtils, AuthenticationManager authenticationManager, UserService userService, ModelMapper modelMapper) {
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
                                                              HttpServletResponse response) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername(), user.getUserType().toString());
        int expiresIn = tokenUtils.getExpiredIn();
        UserTokenState userTokenState = new UserTokenState(jwt,expiresIn);

        return ResponseEntity.ok(new LoginDTO(userTokenState.getAccess_token(),userTokenState.getExpires_in(),user.getUserType(), user.getId()));
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<Boolean> checkIfUsernameIsAvailable(@PathVariable String username){
        //vraca false ako je zauzeto ime (vec postoji korisnik)
        return new ResponseEntity<>(userService.checkIfUsernameIsAvailable(username), HttpStatus.OK);
    }

    @GetMapping("/role")
    public ResponseEntity<String> getRole(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        return ResponseEntity.ok(tokenUtils.getRoleFromToken(token));
    }

    // U slucaju isteka vazenja JWT tokena, endpoint koji se poziva da se token osvezi
    @PostMapping(value = "/refresh")
    public ResponseEntity<UserTokenState> refreshAuthenticationToken(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = this.tokenUtils.getUsernameFromToken(token);
        User user = (User) this.userService.loadUserByUsername(username);
        String refreshedToken = tokenUtils.refreshToken(token);
        int expiresIn = tokenUtils.getExpiredIn();
        return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
    }
    
}
