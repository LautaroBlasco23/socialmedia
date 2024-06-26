package socialmedia.backend.security.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import socialmedia.backend.user.userAuth.service.UserAuthService;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private final JwtEncoder jwtEncoder;
    private final UserAuthService userAuthService;

    public String generateAccessToken(Authentication authentication) throws UserNotFoundException {
        String role = getRolesOfUser(authentication);
        Long userAuthId = this.userAuthService.getUserIdWithEmail(authentication.getName());

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("lautaro")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(60 , ChronoUnit.MINUTES))
            .subject(userAuthId.toString())
            .claim("role", role)
            .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(Authentication authentication) {
        
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("lautaro")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(7 , ChronoUnit.DAYS))
            .subject(authentication.getName())
            .claim("scope", "REFRESH_TOKEN")
            .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private static String getRolesOfUser(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }
}
