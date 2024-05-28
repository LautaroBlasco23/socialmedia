package socialmedia.backend.security.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import socialmedia.backend.security.jwt.TokenType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    @JsonProperty("access_token")
    private String accessToken;
    
    @JsonProperty("access_token_expiry")
    private int accessTokenExpiry;

    @JsonProperty("token_type")
    private TokenType tokenType;
    
    @JsonProperty("user_id")
    private Long userId;
}
