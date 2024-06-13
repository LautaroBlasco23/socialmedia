package socialmedia.backend.posts.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashSet;

import lombok.RequiredArgsConstructor;
import socialmedia.backend.posts.dtos.CreatePostDTO;
import socialmedia.backend.posts.dtos.PostReturnDTO;
import socialmedia.backend.posts.entity.PostEntity;
import socialmedia.backend.posts.exceptions.PostNotFound;
import socialmedia.backend.posts.mapper.PostMapper;
import socialmedia.backend.posts.repository.PostRepository;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;
import socialmedia.backend.user.userProfile.exceptions.UserNotFoundException;
import socialmedia.backend.user.userProfile.repository.UserProfileRepository;

@Service
@RequiredArgsConstructor
public class PostService {
    
    private final PostRepository postRepository;
    private final UserProfileRepository userProfileRepository;

    public List<PostReturnDTO> getAllPosts() {
        List<PostEntity> listOfPosts = this.postRepository.findAll();
        
        List<PostReturnDTO> listToReturn = new ArrayList<>();
        for (PostEntity post: listOfPosts) {
            listToReturn.add(PostMapper.FromEntityToDTO(post));
        }

        return listToReturn;
    }

    public List<PostReturnDTO> getAllUserPosts(Long userId) throws UserNotFoundException {
        Optional<UserProfileEntity> userQuery = userProfileRepository.findById(userId);
        if (userQuery.isEmpty()) {
            throw new UserNotFoundException();
        }

        UserProfileEntity user = userQuery.get();
        List<PostEntity> listOfPosts = postRepository.findAllByUser(user);

        List<PostReturnDTO> listToReturn = new ArrayList<>();
        for (PostEntity post: listOfPosts) {
            listToReturn.add(PostMapper.FromEntityToDTO(post));
        }

        return listToReturn;
    }

    public PostReturnDTO getPostById(Long postId) throws PostNotFound {
        Optional<PostEntity> postQuery = postRepository.findById(postId);
        if (postQuery.isEmpty()) {
            throw new PostNotFound();
        }
        return PostMapper.FromEntityToDTO(postQuery.get());
    }

    public PostReturnDTO createNewPost(CreatePostDTO postData, Long userId) throws UserNotFoundException {
        Optional<UserProfileEntity> userQuery = userProfileRepository.findById(userId);
        if (userQuery.isEmpty()) {
            throw new UserNotFoundException();
        }
        
        UserProfileEntity user = userQuery.get();
        PostEntity newPost = PostEntity.builder()
            .user(user)
            .text(postData.getText())
            .listOfComments(new HashSet<>())
            .usersThatLiked(new HashSet<>())
            .build();
            
        newPost = postRepository.save(newPost);
        return PostMapper.FromEntityToDTO(newPost);
    }

    public PostReturnDTO modifyTextInPost(Long postId, CreatePostDTO postData) throws PostNotFound {
        Optional<PostEntity> postQuery = postRepository.findById(postId);
        if (postQuery.isEmpty()) {
            throw new PostNotFound();
        }

        PostEntity postToModify = postQuery.get();
        postToModify.setText(postData.getText());
        
        postRepository.save(postToModify);
        return PostMapper.FromEntityToDTO(postToModify);
    }

    public PostReturnDTO likePost(Long postId, Long userId) throws PostNotFound, UserNotFoundException {
        Optional<PostEntity> postQuery = postRepository.findById(postId);
        if (postQuery.isEmpty()) {
            throw new PostNotFound();
        }
        Optional<UserProfileEntity> userQuery = userProfileRepository.findById(userId);
        if (userQuery.isEmpty()) {
            throw new UserNotFoundException();
        }

        PostEntity postToModify = postQuery.get();
        UserProfileEntity user = userQuery.get();

        // if user already liked the comment, the app will remove user's like.
        if (postToModify.getUsersThatLiked().contains(user)) {
            postToModify.getUsersThatLiked().remove(user);
        } else {
            postToModify.getUsersThatLiked().add(user);
        }

        postRepository.save(postToModify);
        return PostMapper.FromEntityToDTO(postToModify);
    }

    public PostReturnDTO deletePost(Long postId) throws PostNotFound {
        Optional<PostEntity> postQuery = this.postRepository.findById(postId);
        
        if (postQuery.isEmpty()) {
            throw new PostNotFound();
        }

        postRepository.delete(postQuery.get());
        return PostMapper.FromEntityToDTO(postQuery.get());
    }
}