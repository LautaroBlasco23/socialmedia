package socialmedia.backend.user.userProfile.mapper;

import socialmedia.backend.user.userProfile.dtos.UserReturnDTO;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;

public class UserProfileMapper {
    
    public static UserReturnDTO fromEntityToDTO(UserProfileEntity userProfileEntity) {
        return UserReturnDTO.builder()
        .id(userProfileEntity.getId())
        .firstname(userProfileEntity.getFirstname())
        .lastname(userProfileEntity.getLastname())
        .posts(userProfileEntity.getUserPosts().size())
        .comments(userProfileEntity.getUserComments().size())
        .following(userProfileEntity.getFollowing().size())
        .likedPosts(userProfileEntity.getLikedPosts().size())
        .likedComments(userProfileEntity.getLikedComments().size())
        .build();
    }
}