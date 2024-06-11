package socialmedia.backend.comments.mapper;

import socialmedia.backend.comments.dtos.CommentReturnDTO;
import socialmedia.backend.comments.entity.CommentEntity;

public class CommentMapper {
    
    public static CommentReturnDTO fromEntityToDTO(CommentEntity commentData) {
        return CommentReturnDTO.builder()
            .id(commentData.getId())
            .text(commentData.getText())
            .userId(commentData.getUser().getId())
            .postId(commentData.getPost().getId())
            .QuantityOfLikes(commentData.getLikes().size())
            .quantityOfReplies(commentData.getReplies().size())
            .build();
    }
}
