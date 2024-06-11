package socialmedia.backend.posts.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.HashSet;

import lombok.RequiredArgsConstructor;
import socialmedia.backend.posts.dtos.CreatePostDTO;
import socialmedia.backend.posts.entity.PostEntity;
import socialmedia.backend.posts.exceptions.PostNotFound;
import socialmedia.backend.posts.repository.PostRepository;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;
import socialmedia.backend.user.userProfile.service.UserProfileService;

@Service
@RequiredArgsConstructor
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

    public PostEntity createNewPost(CreatePostDTO postData, Long userId) throws UserNotFoundException {
        UserProfileEntity user = this.userProfileService.getProfileById(userId);
        PostEntity newPost = PostEntity.builder()
            .user(user)
            .text(postData.getText())
            .listOfComments(new HashSet<>())
            .usersThatLiked(new HashSet<>())
            .build();
            
        newPost = this.postRepository.save(newPost);
        return newPost;
    }

    public PostEntity modifyTextInPost(Long postId, CreatePostDTO postData) throws PostNotFound {
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
        UserProfileEntity user = this.userProfileService.getProfileById(userId);

        // if user already liked the comment, the app will remove user's like.
        if (postToModify.getUsersThatLiked().contains(user)) {
            postToModify.getUsersThatLiked().remove(user);
        } else {
            postToModify.getUsersThatLiked().add(user);
        }

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