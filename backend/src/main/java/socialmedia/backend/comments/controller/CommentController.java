package socialmedia.backend.comments.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import socialmedia.backend.comments.dtos.NewCommentDTO;
import socialmedia.backend.comments.entity.CommentEntity;
import socialmedia.backend.comments.exceptions.CommentNotFound;
import socialmedia.backend.comments.exceptions.InvalidComment;
import socialmedia.backend.comments.exceptions.UnAuthorizedEditor;
import socialmedia.backend.comments.service.CommentService;
import socialmedia.backend.posts.exceptions.PostNotFound;
import socialmedia.backend.security.jwt.JwtTokenUtils;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenUtils jwtTokenUtils;

    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getPostComments(@PathVariable Long postId) {
        try {
            List<CommentEntity> listOfComments = this.commentService.getPostComments(postId);
            return ResponseEntity.ok(listOfComments);            
        } catch (PostNotFound e) {
            return ResponseEntity.badRequest().body("post not found");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserComments(@PathVariable Long userId) {
        try {
            List<CommentEntity> listOfComments = this.commentService.getUserComments(userId);
            return ResponseEntity.ok(listOfComments);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("user not found");
        }
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable Long commentId) {
        try {
            CommentEntity comment = this.commentService.getCommentById(commentId);
            return ResponseEntity.ok(comment);
        } catch (CommentNotFound e) {
            return ResponseEntity.badRequest().body("comment not found");
        }
    }

    // getting an error, use logs to know where is it.
    @PostMapping("/post/{postId}")
    public ResponseEntity<?> createComment(@PathVariable Long postId, @RequestBody NewCommentDTO commentData, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            String commentText = commentData.getText();

            CommentEntity newComment = this.commentService.createComment(commentText, userId, postId);

            return ResponseEntity.ok(newComment);
        } catch (PostNotFound e) {
            return ResponseEntity.badRequest().body("post not found");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body("author not found");
        } catch (InvalidComment e) {
            return ResponseEntity.badRequest().body("please insert a valid text");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("something went wrong in our server, we'll solve it as soon as possible");
        }
    }

    @PostMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<?> replyComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody NewCommentDTO commentData, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            String commentText = commentData.getText();

            CommentEntity newComment = this.commentService.createReply(commentText, userId, postId, commentId);

            return ResponseEntity.ok(newComment);
        } catch (PostNotFound e) {
            return ResponseEntity.badRequest().body("the post in which you want to comment couldn't be found");
        } catch (CommentNotFound e) {
            return ResponseEntity.badRequest().body("we can't find the comment you want to reply to");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("invalid token, user not found");
        } catch (InvalidComment e) {
            return ResponseEntity.badRequest().body("please insert a valid text");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("something went wrong in our server, we'll solve it as soon as possible");
        }
    }

    @PutMapping("modify/{commentId}")
    public ResponseEntity<?> modifyComment(@PathVariable Long commentId, @RequestBody NewCommentDTO commentData, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            CommentEntity modifiedComment = this.commentService.modifyComment(commentData, commentId, userId);
            return ResponseEntity.ok(modifiedComment);
        } catch (UnAuthorizedEditor e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized"); 
        } catch (CommentNotFound e) {
            return ResponseEntity.badRequest().body("comment not found");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("something went wrong in our server, we'll solve it as soon as possible");

        }
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            this.commentService.deleteComment(commentId, userId);
            return ResponseEntity.ok("comment with id: " + commentId + " deleted successfully");
        } catch (CommentNotFound e) {
            return ResponseEntity.badRequest().body("comment not found");
        } catch (UnAuthorizedEditor e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthorized");
        }
    }
}