package socialmedia.backend.user.userProfile.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReturnDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private int posts;
    private int comments;
    private int following;
    private int likedPosts;
    private int likedComments;
}