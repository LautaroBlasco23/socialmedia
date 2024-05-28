package socialmedia.backend.security.jwt;

import java.time.Instant;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import socialmedia.backend.security.config.RSAKeyRecord;
import socialmedia.backend.user.userAuth.entity.UserAuthEntity;
import socialmedia.backend.user.userAuth.repository.UserAuthRepository;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    
    private final UserAuthRepository userAuthRepository;
    private final RSAKeyRecord rsaKeyRecord;
    
    public Long getIdFromToken(Jwt jwtToken){
        return Long.parseLong(jwtToken.getSubject());
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails) {
        Long userId = this.getIdFromToken(jwtToken);

        Optional<UserAuthEntity> userData = this.userAuthRepository.findById(userId);
        if (userData.isEmpty()) {
            return false;
        }
        String userEmail = userData.get().getEmail();

        boolean isTokenExpired = this.isTokenExpired(jwtToken);
        boolean isTokenUserSameAsDatabase = userEmail.equals(userDetails.getUsername());
        return !isTokenExpired  && isTokenUserSameAsDatabase;
    }

    private boolean isTokenExpired(Jwt jwtToken) {
        return jwtToken.getExpiresAt().isBefore(Instant.now());
    }

    public UserDetails userDetails(String email) throws UsernameNotFoundException {
        Optional<UserAuthEntity> userData = userAuthRepository.findByEmail(email);
        if (userData.isEmpty()) {
            throw new UsernameNotFoundException("user not found");
        }

        return userData.get();
    }

    public UserDetails userDetails(Long userId) throws UsernameNotFoundException {
        Optional<UserAuthEntity> userData = userAuthRepository.findById(userId);
        if (userData.isEmpty()) {
            throw new UsernameNotFoundException("user not found");
        }

        return userData.get();
    }

    public Jwt getJwtFromTokenString(String stringToken) {
        JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
        Jwt decodedToken = jwtDecoder.decode(stringToken);
        return decodedToken;
    }

    public Long gettingUserIdFromRequest(HttpServletRequest request) throws InvalidBearerTokenException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidBearerTokenException("invalid token");
        }

        String tokenString = authHeader.substring(7);
        Jwt jwtoken = this.getJwtFromTokenString(tokenString);
        Long myUserId = this.getIdFromToken(jwtoken);

        return myUserId;
    }
}
