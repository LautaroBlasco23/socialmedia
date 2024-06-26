package socialmedia.backend.comments.dtos;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class CommentReturnDTO {
    private Long id;
    private String text;
    private Long userId;
    private Long postId;
    private int quantityOfLikes;
}