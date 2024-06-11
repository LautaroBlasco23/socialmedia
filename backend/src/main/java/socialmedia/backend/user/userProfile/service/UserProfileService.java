package socialmedia.backend.user.userProfile.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import socialmedia.backend.user.userAuth.entity.UserAuthEntity;
import socialmedia.backend.user.userProfile.dtos.CreateProfileDTO;
import socialmedia.backend.user.userProfile.dtos.UpdateProfileDTO;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;
import socialmedia.backend.user.userProfile.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    
    private final UserProfileRepository userProfileRepository;

    public List<UserProfileEntity> getAllProfiles() {
        return this.userProfileRepository.findAll();
    }

    public UserProfileEntity getProfileById(Long profileId) throws UserNotFoundException {
        Optional<UserProfileEntity> userQuery = this.userProfileRepository.findById(profileId);
        if (userQuery.isEmpty()) {
            throw new UserNotFoundException();
        }
        return userQuery.get();
    }

    public UserProfileEntity createProfile(CreateProfileDTO profileData) {
        UserProfileEntity newProfile = UserProfileEntity.builder()
            .firstname(profileData.getFirstname())
            .lastname(profileData.getLastname())
            .build();

        this.userProfileRepository.save(newProfile);

        return newProfile;
    }

    public UserProfileEntity createDefaultProfile(UserAuthEntity userAuth) {
        UserProfileEntity newProfile = UserProfileEntity.builder()
            .auth(userAuth)
            .following(null)
            .userPosts(null)
            .userComments(null)
            .likedPosts(null)
            .likedComments(null)
            .firstname("default")
            .lastname("default")
            .build();

        this.userProfileRepository.save(newProfile);

        return newProfile;
    }

    public UserProfileEntity modifyProfile(Long profileId, UpdateProfileDTO profileData) throws UserNotFoundException {
        Optional<UserProfileEntity> userQuery = this.userProfileRepository.findById(profileId);

        if (userQuery.isEmpty()) {
            throw new UserNotFoundException();
        }

        UserProfileEntity modifiedUser = userQuery.get();

        modifiedUser.setFirstname(profileData.getFirstname());
        modifiedUser.setLastname(profileData.getLastname());

        this.userProfileRepository.save(modifiedUser);

        return modifiedUser;
    }
}