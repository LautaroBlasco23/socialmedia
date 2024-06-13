package socialmedia.backend.posts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import socialmedia.backend.posts.dtos.CreatePostDTO;
import socialmedia.backend.posts.dtos.PostReturnDTO;
import socialmedia.backend.posts.exceptions.PostNotFound;
import socialmedia.backend.posts.service.PostService;
import socialmedia.backend.security.jwt.JwtTokenUtils;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtTokenUtils jwtTokenUtils;
    
    @GetMapping("")
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok(this.postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getSpecificPost(@PathVariable Long postId) {
        try {
            return ResponseEntity.ok(this.postService.getPostById(postId));
        } catch (PostNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found");
        } catch (Exception e ) {
            return ResponseEntity.badRequest().body("internal server error");
        }
    }

    @GetMapping("/user/{userId}")
    // add user not found exception.
    public ResponseEntity<?> getUserPosts(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(this.postService.getAllUserPosts(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody CreatePostDTO postData, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            return ResponseEntity.ok(this.postService.createNewPost(postData, userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            PostReturnDTO likedPost = this.postService.likePost(postId, userId);
            return ResponseEntity.ok(likedPost);            
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("invalid token, user not found");
        } catch (PostNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/modify/{postId}")
    public ResponseEntity<?> modifyPost(@PathVariable Long postId, @RequestBody CreatePostDTO postData, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            PostReturnDTO post = this.postService.getPostById(postId);
            if (post.getUserId() != userId) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you can't edit this post");
            }

            PostReturnDTO modifiedPost = this.postService.modifyTextInPost(postId, postData);
            return ResponseEntity.ok(modifiedPost);
        } catch (PostNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{postId}")
    // errors in execution, returns internal server error, but the post gets deleted
    public ResponseEntity<?> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            PostReturnDTO post = this.postService.getPostById(postId);
            if (post.getUserId() != userId) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you can't edit this post");
            }
            PostReturnDTO modifiedPost = this.postService.deletePost(postId);
            return ResponseEntity.ok(modifiedPost);
        } catch (PostNotFound e) {
            return ResponseEntity.notFound().build();
        }
    }
}