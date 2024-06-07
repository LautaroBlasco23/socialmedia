package socialmedia.backend.posts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import socialmedia.backend.posts.entity.PostEntity;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findAllByUser(UserProfileEntity user);
}