package socialmedia.backend.comments.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentReturnDTO {
    private Long id;
    private String text;
    private Long userId;
    private Long postId;
    private int QuantityOfLikes;
    private int quantityOfReplies;
}