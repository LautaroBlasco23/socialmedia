package socialmedia.backend.security.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import socialmedia.backend.security.dtos.AuthResponseDTO;
import socialmedia.backend.security.exceptions.EmailAlreadyInUseException;
import socialmedia.backend.security.exceptions.InvalidCredentialsException;
import socialmedia.backend.security.jwt.JwtTokenGenerator;
import socialmedia.backend.security.jwt.TokenType;
import socialmedia.backend.security.jwt.refreshToken.entity.RefreshTokenEntity;
import socialmedia.backend.security.jwt.refreshToken.repository.RefreshTokenRepository;
import socialmedia.backend.user.userAuth.dtos.UserRegisterDTO;
import socialmedia.backend.user.userAuth.entity.UserAuthEntity;
import socialmedia.backend.user.userAuth.mapper.UserAuthMapper;
import socialmedia.backend.user.userAuth.repository.UserAuthRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAuthRepository userAuthRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserAuthMapper userAuthMapper;

    public AuthResponseDTO getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) throws InvalidCredentialsException {
        if (authentication == null) {
            throw new InvalidCredentialsException();
        }   
        
        Optional<UserAuthEntity> userQuery = userAuthRepository.findByEmail(authentication.getName());
        if (userQuery.isEmpty()) {
            throw new InvalidCredentialsException();
        }


        if (!this.comparePasswords(userQuery.get().getPassword(), authentication.getCredentials().toString())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
        String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

        creatRefreshTokenCookie(response, refreshToken);
        saveUserRefreshToken(userQuery.get(),refreshToken);

        return AuthResponseDTO.builder()
            .accessToken(accessToken)
            .accessTokenExpiry(15 * 60)
            .userId(userQuery.get().getId())
            .tokenType(TokenType.Bearer)
            .build();
    }

    public AuthResponseDTO registerUser(UserRegisterDTO userRegisterDTO, HttpServletResponse httpServletResponse) throws EmailAlreadyInUseException {
        Optional<UserAuthEntity> user = userAuthRepository.findByEmail(userRegisterDTO.getEmail());
        if(user.isPresent()){
            throw new EmailAlreadyInUseException();
        }

        // storing user's data into db.
        UserAuthEntity userAuthEntity = userAuthMapper.convertToEntity(userRegisterDTO);
        UserAuthEntity savedUserAuth = userAuthRepository.save(userAuthEntity);
        
        // Generating JWT token
        Authentication authentication = createAuthenticationObject(userAuthEntity);
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
        String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

        saveUserRefreshToken(userAuthEntity, refreshToken);
        creatRefreshTokenCookie(httpServletResponse,refreshToken);
        
        return AuthResponseDTO.builder()
            .accessToken(accessToken)
            .accessTokenExpiry(5 * 60)
            .userId(savedUserAuth.getId())
            .tokenType(TokenType.Bearer)
            .build();
    }

    private void saveUserRefreshToken(UserAuthEntity userAuthEntity, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
            .user(userAuthEntity)
            .refreshToken(refreshToken)
            .revoked(false)
            .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60 ); // in seconds
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {
 
        if(!authorizationHeader.startsWith(TokenType.Bearer.name())){
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        //Find refreshToken from database and should not be revoked : Same thing can be done through filter.  
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
            .filter(tokens-> !tokens.isRevoked())
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));

        UserAuthEntity userAuthEntity = refreshTokenEntity.getUser();
        
        //Now create the Authentication object
        Authentication authentication =  createAuthenticationObject(userAuthEntity);

        //Use the authentication object to generate new accessToken as the Authentication object that we will have may not contain correct role. 
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return AuthResponseDTO.builder()
            .accessToken(accessToken)
            .accessTokenExpiry(5 * 60)
            .userId(userAuthEntity.getId())
            .tokenType(TokenType.Bearer)
            .build();
    }

    private static Authentication createAuthenticationObject(UserAuthEntity userAuthEntity) {
        // Extract user details from UserDetailsEntity
        String email = userAuthEntity.getEmail();
        String password = userAuthEntity.getPassword();
        String roles = userAuthEntity.getRoles();

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
            .map(role -> (GrantedAuthority) role::trim)
            .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(email, password, Arrays.asList(authorities));
    }

    private boolean comparePasswords(String encryptedPassword, String plainPassword) {
        return new BCryptPasswordEncoder().matches(plainPassword, encryptedPassword);
    }
}
