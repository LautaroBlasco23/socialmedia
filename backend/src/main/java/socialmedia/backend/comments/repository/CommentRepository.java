package socialmedia.backend.comments.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import socialmedia.backend.comments.entity.CommentEntity;
import socialmedia.backend.posts.entity.PostEntity;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByUser(UserProfileEntity authorId);
    List<CommentEntity> findByPost(PostEntity post);
    List<CommentEntity> findByParentComment(CommentEntity parentComment);
}
