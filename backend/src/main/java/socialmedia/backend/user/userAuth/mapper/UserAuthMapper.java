package socialmedia.backend.user.userAuth.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import socialmedia.backend.user.userAuth.dtos.UserRegisterDTO;
import socialmedia.backend.user.userAuth.entity.UserAuthEntity;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;

@Component
@RequiredArgsConstructor
public class UserAuthMapper {

    private final PasswordEncoder passwordEncoder;
    
    public UserAuthEntity convertToEntity(UserRegisterDTO userData) {
        UserAuthEntity userAuthEntity = UserAuthEntity.builder()
            .email(userData.getEmail())
            .password(passwordEncoder.encode(userData.getPassword()))
            .roles("user")
            .userProfile(UserProfileEntity.defaultBuild())
            .isAccountNonExpired(true)
            .isAccountNonLocked(true)
            .isCredentialsNonExpired(true)
            .isEnabled(true)
            .build();

        return userAuthEntity;
    }
}