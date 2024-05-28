package socialmedia.backend.user.userAuth.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import socialmedia.backend.security.jwt.refreshToken.repository.RefreshTokenRepository;
import socialmedia.backend.user.userAuth.entity.UserAuthEntity;
import socialmedia.backend.user.userAuth.repository.UserAuthRepository;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserDetailsService {
    
    private final UserAuthRepository userAuthRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserAuthEntity> userData = userAuthRepository.findByEmail(email);

        if (userData.isEmpty()) {
            throw new UsernameNotFoundException("we don't have an user with that email!");
        }

        return userData.get();
    }

    public UserAuthEntity changePassword(Long userId, String newPassword) throws UsernameNotFoundException {
        Optional<UserAuthEntity> userData = this.userAuthRepository.findById(userId);
        if (userData.isEmpty()) {
            throw new UsernameNotFoundException("we dont have an user with that id");
        }

        userData.get().setPassword(encryptPassword(newPassword));
        this.userAuthRepository.save(userData.get());

        return userData.get();
    }

    public Long getUserIdWithEmail(String email) throws UsernameNotFoundException {
        Optional<UserAuthEntity> userData = this.userAuthRepository.findByEmail(email);
        if (userData.isEmpty()) {
            throw new UsernameNotFoundException("we don't have an user with that email!");
        }
        return userData.get().getId();
    }

    public String deleteUserWithId(Long id) throws UsernameNotFoundException {
        Optional<UserAuthEntity> userData = this.userAuthRepository.findById(id);
        if (userData.isEmpty()) {
            throw new UsernameNotFoundException("we don't have an user with that email!");
        }

        this.refreshTokenRepository.deleteTokensByUserId(id);
        this.userAuthRepository.delete(userData.get());
        return "User with email: " + userData.get().getEmail() + " deleted";
    }

    public Long getProfileIdWithEmail(String email) throws UsernameNotFoundException {
        Optional<UserAuthEntity> userQuery = this.userAuthRepository.findByEmail(email);
        if (userQuery.isEmpty()) {
            throw new UsernameNotFoundException("we don't have an user with that email!");
        }
        return userQuery.get().getUserProfile().getId();
    }

    private String encryptPassword(String plainPassword) {
        return new BCryptPasswordEncoder().encode(plainPassword);
    }

}