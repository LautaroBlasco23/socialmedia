package socialmedia.backend.posts.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import socialmedia.backend.comments.entity.CommentEntity;
import socialmedia.backend.posts.dtos.PostDataDTO;
import socialmedia.backend.posts.entity.PostEntity;
import socialmedia.backend.posts.exceptions.PostNotFound;
import socialmedia.backend.posts.repository.PostRepository;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;
import socialmedia.backend.user.userProfile.service.UserProfileService;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    
    private final PostRepository postRepository;
    private final UserProfileService userProfileService;

    public List<PostEntity> getAllPosts() {
        List<PostEntity> listOfPosts = this.postRepository.findAll();
        return listOfPosts;
    }

    public List<PostEntity> getAlllUserPosts(Long userId) throws UserNotFoundException {
        UserProfileEntity user = this.userProfileService.getProfileById(userId);
        List<PostEntity> listOfPosts = this.postRepository.findAllByUser(user);
        return listOfPosts;
    }

    public PostEntity getPostById(Long postId) throws PostNotFound {
        Optional<PostEntity> postQuery = this.postRepository.findById(postId);
        if (postQuery.isEmpty()) {
            throw new PostNotFound();
        }
        return postQuery.get();
    }

    public PostEntity createNewPost(PostDataDTO postData, Long userId) throws UserNotFoundException {
        UserProfileEntity user = this.userProfileService.getProfileById(userId);
        log.info("user: " + user.toString());
        PostEntity newPost = PostEntity.builder()
            .user(user)
            .text(postData.getText())
            .listOfComments(new HashSet<>())
            .likes(new HashSet<>())
            .build();

        this.postRepository.save(newPost);
        return newPost;
    }

    public PostEntity modifyTextInPost(Long postId, PostDataDTO postData) throws PostNotFound {
        Optional<PostEntity> postQuery = this.postRepository.findById(postId);
        if (postQuery.isEmpty()) {
            throw new PostNotFound();
        }
        PostEntity postToModify = postQuery.get();
        postToModify.setText(postData.getText());
        this.postRepository.save(postToModify);
        return postToModify;
    }

    public PostEntity likePost(Long postId, Long userId) throws PostNotFound, UserNotFoundException {
        Optional<PostEntity> postQuery = this.postRepository.findById(postId);
        if (postQuery.isEmpty()) {
            throw new PostNotFound();
        }
        PostEntity postToModify = postQuery.get();

        // if user already liked the comment, the app will remove user's like.
        if (postToModify.getLikes().contains(userId)) {
            postToModify.getLikes().remove(userId);
        } else {
            postToModify.getLikes().add(userId);
        }

        this.postRepository.save(postToModify);
        return postToModify;
    }

    public PostEntity addCommentToPost(Long postId, CommentEntity newComment) throws PostNotFound {
        Optional<PostEntity> postQuery = this.postRepository.findById(postId);
        if (postQuery.isEmpty()) {
            throw new PostNotFound();
        }
        PostEntity postToModify = postQuery.get();
        postToModify.getListOfComments().add(newComment);
        this.postRepository.save(postToModify);

        return postToModify;
    }

    public PostEntity deletePost(Long postId) throws PostNotFound {
        Optional<PostEntity> postQuery = this.postRepository.findById(postId);
        if (postQuery.isEmpty()) {
            throw new PostNotFound();
        }
        this.postRepository.delete(postQuery.get());
        return postQuery.get();
    }
}