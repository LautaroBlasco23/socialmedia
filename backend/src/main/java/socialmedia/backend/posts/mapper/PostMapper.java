package socialmedia.backend.posts.mapper;

import socialmedia.backend.posts.dtos.PostReturnDTO;
import socialmedia.backend.posts.entity.PostEntity;

public class PostMapper {
    
    public static PostReturnDTO FromEntityToDTO(PostEntity postData) {
        return PostReturnDTO.builder()
            .id(postData.getId())
            .userId(postData.getUser().getId())
            .text(postData.getText())
            .numberOfComments(postData.getListOfComments().size())
            .numberOfLikes(postData.getUsersThatLiked().size())
            .build();
    }

}
