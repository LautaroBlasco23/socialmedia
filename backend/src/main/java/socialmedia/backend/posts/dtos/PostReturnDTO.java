package socialmedia.backend.posts.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostReturnDTO {
    private Long id;
    private String text;
    private Long userId;
    private int numberOfLikes;
    private int numberOfComments;
}