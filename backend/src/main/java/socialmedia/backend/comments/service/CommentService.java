package socialmedia.backend.comments.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import lombok.RequiredArgsConstructor;
import socialmedia.backend.comments.dtos.NewCommentDTO;
import socialmedia.backend.comments.entity.CommentEntity;
import socialmedia.backend.comments.exceptions.CommentNotFound;
import socialmedia.backend.comments.exceptions.InvalidComment;
import socialmedia.backend.comments.exceptions.UnAuthorizedEditor;
import socialmedia.backend.comments.repository.CommentRepository;
import socialmedia.backend.posts.entity.PostEntity;
import socialmedia.backend.posts.exceptions.PostNotFound;
import socialmedia.backend.posts.service.PostService;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;
import socialmedia.backend.user.userProfile.service.UserProfileService;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final UserProfileService userProfileService;
    private final PostService postService;

    public List<CommentEntity> getAllComments() {
        return this.commentRepository.findAll();
    }

    public CommentEntity getCommentById(Long commentId) throws CommentNotFound {
        Optional<CommentEntity> commentQuery = this.commentRepository.findById(commentId);
        if (commentQuery.isEmpty()) {
            throw new CommentNotFound();
        }
        return commentQuery.get();
    }

    public List<CommentEntity> getUserComments(Long userId) throws UserNotFoundException {
        UserProfileEntity user = this.userProfileService.getProfileById(userId);
        List<CommentEntity> listOfComments = this.commentRepository.findByUser(user);
        return listOfComments;
    }

    public List<CommentEntity> getPostComments(Long postId) throws PostNotFound {
        PostEntity post = this.postService.getPostById(postId);        
        List<CommentEntity> listOfComments = this.commentRepository.findByPost(post);
        return listOfComments;
    }

    public CommentEntity createComment(String commentText, Long userId, Long postId) throws InvalidComment, PostNotFound, UserNotFoundException {
        
        if (commentText == null || commentText.length() == 0) {
            throw new InvalidComment();
        }

        PostEntity post = this.postService.getPostById(postId);
        UserProfileEntity user = this.userProfileService.getProfileById(userId);
        CommentEntity newComment = CommentEntity.builder()
            .post(post)
            .user(user)
            .text(commentText)
            .likes(new HashSet<Long>())
            .build();
        
        this.commentRepository.save(newComment);
        this.postService.addCommentToPost(postId, newComment);

        return newComment;
    }

    public CommentEntity createReply(String commentText, Long userId, Long postId, Long commentId) throws InvalidComment, PostNotFound, UserNotFoundException, CommentNotFound {
        
        if (commentText == null || commentText.length() == 0) {
            throw new InvalidComment();
        }

        CommentEntity commentToReply = this.getCommentById(commentId);
        PostEntity post = this.postService.getPostById(postId);
        UserProfileEntity user = this.userProfileService.getProfileById(userId);

        CommentEntity newComment = CommentEntity.builder()
            .post(post)
            .user(user)
            .text(commentText)
            .likes(new HashSet<Long>())
            .build();
        
        commentToReply.getReplies().add(newComment.getId());
        this.commentRepository.save(newComment);
        this.commentRepository.save(commentToReply);

        return newComment;
    }
    
    public CommentEntity modifyComment(NewCommentDTO newComment, Long commentId, Long userId) throws CommentNotFound, UnAuthorizedEditor {
        Optional<CommentEntity> commentQuery = this.commentRepository.findById(commentId);
        if (commentQuery.isEmpty()) {
            throw new CommentNotFound(); 
        }
        
        CommentEntity commentToModify = commentQuery.get();
        if (!commentToModify.getUser().getAuth().getId().equals(userId)) {
            throw new UnAuthorizedEditor();
        }

        commentToModify.setText(newComment.getText());
        this.commentRepository.save(commentToModify);
        return commentToModify;
    }

    public CommentEntity likeComment(NewCommentDTO newComment, Long commentId, Long userId) throws CommentNotFound, UserNotFoundException {
        CommentEntity comment = this.getCommentById(commentId);
        comment.getLikes().add(userId);
        this.commentRepository.save(comment);
        return comment;
    }

    public CommentEntity unLikeComment(NewCommentDTO newComment, Long commentId, Long userId) throws CommentNotFound, UserNotFoundException {
        CommentEntity comment = this.getCommentById(commentId);
        comment.getLikes().remove(userId);
        this.commentRepository.save(comment);
        return comment;
    }

    public CommentEntity deleteComment(Long commentId, Long userId) throws CommentNotFound, UnAuthorizedEditor {
        CommentEntity comment = this.getCommentById(commentId);
        if (!comment.getUser().getAuth().getId().equals(userId)) {
            throw new UnAuthorizedEditor();
        }
        this.commentRepository.delete(comment);
        return comment;
    }
}