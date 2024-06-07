package socialmedia.backend.user.userAuth.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import socialmedia.backend.user.userAuth.dtos.UserAuthDTO;
import socialmedia.backend.user.userAuth.entity.UserAuthEntity;

@Component
@RequiredArgsConstructor
public class UserAuthMapper {
    public UserAuthEntity convertToEntity(UserAuthDTO userData) {
        UserAuthEntity userAuthEntity = UserAuthEntity.builder()
            .email(userData.getEmail())
            .password(userData.getPassword())
            .roles("user")
            .isAccountNonExpired(true)
            .isAccountNonLocked(true)
            .isCredentialsNonExpired(true)
            .isEnabled(true)
            .build();
        return userAuthEntity;
    }
}