package socialmedia.backend.user.userAuth.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import socialmedia.backend.security.jwt.refreshToken.repository.RefreshTokenRepository;
import socialmedia.backend.user.userAuth.dtos.UserAuthDTO;
import socialmedia.backend.user.userAuth.entity.UserAuthEntity;
import socialmedia.backend.user.userAuth.mapper.UserAuthMapper;
import socialmedia.backend.user.userAuth.repository.UserAuthRepository;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;

@Service
@RequiredArgsConstructor
public class UserAuthService implements UserDetailsService {
    
    private final UserAuthMapper userAuthMapper; 
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

    public UserAuthEntity createUserAuthEntity(UserAuthDTO userAuthData) {
        UserAuthEntity newUserAuth = userAuthMapper.convertToEntity(userAuthData);
        
        return this.userAuthRepository.save(newUserAuth);
    }

    public UserAuthEntity changePassword(Long userId, String newPassword) throws UserNotFoundException {
        Optional<UserAuthEntity> userData = this.userAuthRepository.findById(userId);
        if (userData.isEmpty()) {
            throw new UserNotFoundException();
        }

        userData.get().setPassword(encryptPassword(newPassword));
        this.userAuthRepository.save(userData.get());

        return userData.get();
    }

    public Long getUserIdWithEmail(String email) throws UserNotFoundException {
        Optional<UserAuthEntity> userData = this.userAuthRepository.findByEmail(email);
        if (userData.isEmpty()) {
            throw new UserNotFoundException();
        }
        return userData.get().getId();
    }

    public UserAuthEntity getUserAuthWithEmail(String email) throws UserNotFoundException {
        Optional<UserAuthEntity> userData = this.userAuthRepository.findByEmail(email);
        if (userData.isEmpty()) {
            throw new UserNotFoundException();
        }
        return userData.get();
    }

    public String deleteUserWithId(Long id) throws UserNotFoundException {
        Optional<UserAuthEntity> userData = this.userAuthRepository.findById(id);
        if (userData.isEmpty()) {
            throw new UserNotFoundException();
        }

        this.refreshTokenRepository.deleteTokensByUserId(id);
        this.userAuthRepository.delete(userData.get());
        return "User with email: " + userData.get().getEmail() + " deleted";
    }

    private String encryptPassword(String plainPassword) {
        return new BCryptPasswordEncoder().encode(plainPassword);
    }

}