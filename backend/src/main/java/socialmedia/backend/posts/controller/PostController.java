package socialmedia.backend.posts.controller;

import java.util.List;

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
import lombok.extern.slf4j.Slf4j;
import socialmedia.backend.posts.dtos.PostDataDTO;
import socialmedia.backend.posts.entity.PostEntity;
import socialmedia.backend.posts.exceptions.PostNotFound;
import socialmedia.backend.posts.service.PostService;
import socialmedia.backend.security.jwt.JwtTokenUtils;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
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
            return ResponseEntity.badRequest().body("post not found");
        }
    }

    @GetMapping("/user/{userId}")
    // add user not found exception.
    public ResponseEntity<?> getUserPosts(@PathVariable Long userId) {
        try {
            List<PostEntity> listOfPosts = this.postService.getAlllUserPosts(userId);
            return ResponseEntity.ok(listOfPosts);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("user not found");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostDataDTO postData, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            log.info("userId: " + userId);
            PostEntity newPost = this.postService.createNewPost(postData, userId);
            log.info("newPost: " + newPost.toString());
            return ResponseEntity.ok(newPost);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("invalid user");
        }
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            PostEntity likedPost = this.postService.likePost(postId, userId);
            return ResponseEntity.ok(likedPost);            
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body("invalid token, user not found");
        } catch (PostNotFound e) {
            return ResponseEntity.badRequest().body("post not found");
        }
    }

    @PutMapping("/modify/{postId}")
    public ResponseEntity<?> modifyPost(@PathVariable Long postId, @RequestBody PostDataDTO postData, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            PostEntity post = this.postService.getPostById(postId);
            if (post.getUser().getId() != userId) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you can't edit this post");
            }
            PostEntity modifiedPost = this.postService.modifyTextInPost(postId, postData);
            return ResponseEntity.ok(modifiedPost);
        } catch (PostNotFound e) {
            return ResponseEntity.badRequest().body("post not found");
        }
    }

    @DeleteMapping("/delete/{postId}")
    // errors in execution, returns internal server error, but the post gets deleted
    public ResponseEntity<?> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        try {
            Long userId = this.jwtTokenUtils.getUserIdFromRequest(request);
            PostEntity post = this.postService.getPostById(postId);
            if (post.getUser().getId() != userId) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you can't edit this post");
            }
            PostEntity modifiedPost = this.postService.deletePost(postId);
            return ResponseEntity.ok(modifiedPost);
        } catch (PostNotFound e) {
            return ResponseEntity.badRequest().body("post not found");
        }
    }
}