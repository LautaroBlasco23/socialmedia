package socialmedia.backend.user.userAuth.controllers;

import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import socialmedia.backend.security.dtos.AuthResponseDTO;
import socialmedia.backend.security.exceptions.EmailAlreadyInUseException;
import socialmedia.backend.security.jwt.JwtTokenUtils;
import socialmedia.backend.security.service.AuthService;
import socialmedia.backend.user.userAuth.dtos.UpdatePasswordDTO;
import socialmedia.backend.user.userAuth.dtos.UserAuthDTO;
import socialmedia.backend.user.userAuth.service.UserAuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserAuthControllers {
    
    private final AuthService authService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserAuthService userAuthService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletResponse response, @RequestBody UserAuthDTO loginData) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword());

            return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication, response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAuthDTO userData, BindingResult bindingResult, HttpServletResponse httpServletResponse) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
        
            AuthResponseDTO response = authService.registerUser(userData ,httpServletResponse);
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyInUseException e) {
            return ResponseEntity.badRequest().body("email already in use");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("internal server error, we will fix it soon.");
        }
    }

    @PutMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestBody UpdatePasswordDTO newPassword, BindingResult bindingResult, HttpServletRequest request) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);

            return ResponseEntity.ok(this.userAuthService.changePassword(userId, newPassword.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("internal server error, we will fix it soon.");
        }
    }

    @DeleteMapping("/delete/me")
    public ResponseEntity<?> deleteMyUser(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("invalid token");
            }
    
            String token = authHeader.substring(7);
    
            Jwt jwtToken = this.jwtTokenUtils.getJwtFromTokenString(token);
            Long id = this.jwtTokenUtils.getIdFromToken(jwtToken);
            
            String deleteMessage = this.userAuthService.deleteUserWithId(id);
    
            return ResponseEntity.ok(deleteMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("user not found");
        }
    }
}