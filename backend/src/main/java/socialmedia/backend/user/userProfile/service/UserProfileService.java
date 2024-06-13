package socialmedia.backend.user.userProfile.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import socialmedia.backend.user.userAuth.entity.UserAuthEntity;
import socialmedia.backend.user.userProfile.dtos.CreateProfileDTO;
import socialmedia.backend.user.userProfile.dtos.UpdateProfileDTO;
import socialmedia.backend.user.userProfile.dtos.UserReturnDTO;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;
import socialmedia.backend.user.userProfile.mapper.UserProfileMapper;
import socialmedia.backend.user.userProfile.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    
    private final UserProfileRepository userProfileRepository;

    public List<UserReturnDTO> getAllProfiles() {
        List<UserProfileEntity> listOfUsers = userProfileRepository.findAll();

        List<UserReturnDTO> returnList = new ArrayList<>();
        for (UserProfileEntity user: listOfUsers) {
            returnList.add(UserProfileMapper.fromEntityToDTO(user));
        }

        return returnList;
    }

    public UserReturnDTO getProfileById(Long profileId) throws UserNotFoundException {
        Optional<UserProfileEntity> userQuery = userProfileRepository.findById(profileId);
        if (userQuery.isEmpty()) {
            throw new UserNotFoundException();
        }

        UserReturnDTO user = UserProfileMapper.fromEntityToDTO(userQuery.get());

        return user;
    }

    public UserReturnDTO createProfile(CreateProfileDTO profileData) {
        UserProfileEntity newProfile = UserProfileEntity.builder()
            .firstname(profileData.getFirstname())
            .lastname(profileData.getLastname())
            .build();
        newProfile = userProfileRepository.save(newProfile);
        UserReturnDTO user = UserProfileMapper.fromEntityToDTO(newProfile); 

        return user;
    }

    public UserReturnDTO modifyProfile(Long profileId, UpdateProfileDTO profileData) throws UserNotFoundException {
        Optional<UserProfileEntity> userQuery = userProfileRepository.findById(profileId);

        if (userQuery.isEmpty()) {
            throw new UserNotFoundException();
        }

        UserProfileEntity modifiedUser = userQuery.get();

        modifiedUser.setFirstname(profileData.getFirstname());
        modifiedUser.setLastname(profileData.getLastname());

        modifiedUser = userProfileRepository.save(modifiedUser);
        UserReturnDTO user = UserProfileMapper.fromEntityToDTO(modifiedUser);

        return user;
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

        userProfileRepository.save(newProfile);

        return newProfile;
    }
}