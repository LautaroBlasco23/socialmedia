package socialmedia.backend.comments.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class ReplyReturnDTO extends CommentReturnDTO {
    private Long id;
    private String text;
    private Long userId;
    private Long postId;
    private Long parentCommentId;
    private int quantityOfLikes;
}