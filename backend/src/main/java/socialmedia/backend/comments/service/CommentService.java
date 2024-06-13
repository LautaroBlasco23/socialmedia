package socialmedia.backend.comments.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashSet;

import lombok.RequiredArgsConstructor;
import socialmedia.backend.comments.dtos.CommentReturnDTO;
import socialmedia.backend.comments.dtos.CreateCommentDTO;
import socialmedia.backend.comments.dtos.ReplyReturnDTO;
import socialmedia.backend.comments.entity.CommentEntity;
import socialmedia.backend.comments.exceptions.CommentNotFound;
import socialmedia.backend.comments.exceptions.InvalidComment;
import socialmedia.backend.comments.exceptions.UnAuthorizedEditor;
import socialmedia.backend.comments.mapper.CommentMapper;
import socialmedia.backend.comments.repository.CommentRepository;
import socialmedia.backend.posts.entity.PostEntity;
import socialmedia.backend.posts.exceptions.PostNotFound;
import socialmedia.backend.posts.repository.PostRepository;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;
import socialmedia.backend.user.userProfile.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final UserProfileRepository userProfileRepository;
    private final PostRepository postRepository;
    
    public CommentReturnDTO getCommentById(Long commentId) throws CommentNotFound {
        Optional<CommentEntity> commentQuery = this.commentRepository.findById(commentId);
        if (commentQuery.isEmpty()) {
            throw new CommentNotFound();
        }
        CommentEntity comment = commentQuery.get();

        if (comment.getParentComment() != null) {
            return CommentMapper.fromEntityToReplyDTO(comment);
        }
        return CommentMapper.fromEntityToCommentDTO(commentQuery.get());
    }

    public List<CommentReturnDTO> getUserComments(Long userId) throws UserNotFoundException {
        Optional<UserProfileEntity> user = userProfileRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        List<CommentEntity> listOfComments = this.commentRepository.findByUser(user.get());
        
        List<CommentReturnDTO> commentsToReturn = new ArrayList<>();
        for (CommentEntity comment: listOfComments) {
            commentsToReturn.add(CommentMapper.fromEntityToCommentDTO(comment));
        }

        return commentsToReturn;
    }

    public List<CommentReturnDTO> getCommentReplies(Long parentCommentId) throws CommentNotFound {
        Optional<CommentEntity> parentCommentQuery = commentRepository.findById(parentCommentId);
        if (parentCommentQuery.isEmpty()) {
            throw new CommentNotFound();
        }

        CommentEntity parentComment = parentCommentQuery.get();
        List<CommentEntity> listOfComments = this.commentRepository.findByParentComment(parentComment);
        
        List<CommentReturnDTO> commentsToReturn = new ArrayList<>();
        for (CommentEntity comment: listOfComments) {
            commentsToReturn.add(CommentMapper.fromEntityToCommentDTO(comment));
        }

        return commentsToReturn;
    }

    public List<CommentReturnDTO> getPostComments(Long postId) throws PostNotFound {
        Optional<PostEntity> post = this.postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new PostNotFound();
        }

        List<CommentEntity> listOfComments = this.commentRepository.findByPost(post.get());
        List<CommentReturnDTO> commentsToReturn = new ArrayList<>();
        for (CommentEntity comment: listOfComments) {
            // We will only return post comments.
            if(!comment.isReply()) {
                commentsToReturn.add(CommentMapper.fromEntityToCommentDTO(comment));
            }
        }

        return commentsToReturn;
    }

    public CommentReturnDTO createComment(String commentText, Long userId, Long postId) throws InvalidComment, PostNotFound, UserNotFoundException {
        
        if (commentText == null || commentText.isEmpty()) {
            throw new InvalidComment();
        }

        Optional<PostEntity> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new PostNotFound();
        }

        Optional<UserProfileEntity> user = userProfileRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        
        CommentEntity newComment = CommentEntity.builder()
            .post(post.get())
            .user(user.get())
            .text(commentText)
            .isReply(false)
            .likes(new HashSet<>())
            .build();
        
        newComment = this.commentRepository.save(newComment);

        return CommentMapper.fromEntityToCommentDTO(newComment);
    }

    public ReplyReturnDTO createReply(String commentText, Long userId, Long postId, Long parentId) throws InvalidComment, PostNotFound, UserNotFoundException, CommentNotFound {
        // Checking if everything is ok.
        if (commentText == null || commentText.length() == 0) {
            throw new InvalidComment();
        }
        Optional<CommentEntity> commentToReplyQuery = commentRepository.findById(parentId);
        if (commentToReplyQuery.isEmpty()) {
            throw new CommentNotFound();
        }
        Optional<PostEntity> postQuery = postRepository.findById(postId);
        if(postQuery.isEmpty()) {
            throw new PostNotFound();
        }
        Optional<UserProfileEntity> userQuery = userProfileRepository.findById(userId);
        if (userQuery.isEmpty()) {
            throw new UserNotFoundException();
        }

        UserProfileEntity user = userQuery.get();
        PostEntity post = postQuery.get();
        CommentEntity commentToReply = commentToReplyQuery.get();

        // Creating and saving comment.
        CommentEntity newComment = CommentEntity.builder()
            .post(post)
            .user(user)
            .text(commentText)
            .likes(new HashSet<>())
            .isReply(true)
            .parentComment(commentToReply)
            .build();
        newComment = commentRepository.save(newComment);
        
        commentRepository.save(commentToReply);
        
        return CommentMapper.fromEntityToReplyDTO(newComment);
    }
    
    public CommentReturnDTO modifyComment(CreateCommentDTO newComment, Long commentId, Long userId) throws CommentNotFound, UnAuthorizedEditor {
        Optional<CommentEntity> commentQuery = commentRepository.findById(commentId);
        if (commentQuery.isEmpty()) {
            throw new CommentNotFound(); 
        }
        CommentEntity commentToModify = commentQuery.get();
        if (!commentToModify.getUser().getAuth().getId().equals(userId)) {
            throw new UnAuthorizedEditor();
        }

        commentToModify.setText(newComment.getText());
        commentToModify = commentRepository.save(commentToModify);
        return CommentMapper.fromEntityToCommentDTO(commentToModify);
    }

    public CommentReturnDTO likeComment(CreateCommentDTO newComment, Long commentId, Long userId) throws CommentNotFound, UserNotFoundException {
        Optional<CommentEntity> commentQuery = commentRepository.findById(commentId);
        if (commentQuery.isEmpty()) {
            throw new CommentNotFound();
        }
        Optional<UserProfileEntity> userQuery = userProfileRepository.findById(userId);
        if (userQuery.isEmpty()) {
            throw new UserNotFoundException();
        }

        CommentEntity comment = commentQuery.get();
        UserProfileEntity user = userQuery.get();

        if (comment.getLikes().contains(user)) {
            comment.getLikes().remove(user);
            user.getLikedComments().remove(comment);
        } else {
            comment.getLikes().add(user);
            user.getLikedComments().add(comment);
        }

        commentRepository.save(comment);
        userProfileRepository.save(user);

        return CommentMapper.fromEntityToCommentDTO(comment);
    }

    public CommentReturnDTO deleteComment(Long commentId, Long userId) throws CommentNotFound, UnAuthorizedEditor {
        Optional<CommentEntity> commentQuery = commentRepository.findById(commentId);
        if (commentQuery.isEmpty()) {
            throw new CommentNotFound();
        }
        CommentEntity comment = commentQuery.get();
        if (!comment.getUser().getId().equals(userId)) {
            throw new UnAuthorizedEditor();
        }

        this.commentRepository.delete(comment);
        return CommentMapper.fromEntityToCommentDTO(comment);
    }
}