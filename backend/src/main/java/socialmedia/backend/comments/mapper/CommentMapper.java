package socialmedia.backend.comments.mapper;

import socialmedia.backend.comments.dtos.CommentReturnDTO;
import socialmedia.backend.comments.dtos.ReplyReturnDTO;
import socialmedia.backend.comments.entity.CommentEntity;

public class CommentMapper {
    
    public static CommentReturnDTO fromEntityToCommentDTO(CommentEntity commentData) {
        return CommentReturnDTO.builder()
            .id(commentData.getId())
            .text(commentData.getText())
            .userId(commentData.getUser().getId())
            .postId(commentData.getPost().getId())
            .quantityOfLikes(commentData.getLikes().size())
            .build();
    }

    public static ReplyReturnDTO fromEntityToReplyDTO(CommentEntity commentData) {
        return ReplyReturnDTO.builder()
            .id(commentData.getId())
            .text(commentData.getText())
            .userId(commentData.getUser().getId())
            .postId(commentData.getPost().getId())
            .quantityOfLikes(commentData.getLikes().size())
            .parentCommentId(commentData.getParentComment().getId())
            .build();
    }
}