package socialmedia.backend.user.userProfile.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import socialmedia.backend.comments.entity.CommentEntity;
import socialmedia.backend.posts.entity.PostEntity;
import socialmedia.backend.user.userAuth.entity.UserAuthEntity;

@Entity
@Table(name = "users_profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "auth_id",referencedColumnName = "id")
    private UserAuthEntity auth;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<PostEntity> userPosts;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<CommentEntity> userComments;

    // User follows and followers

    @ManyToMany
    @JoinTable(name = "user_following",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private List<UserProfileEntity> following;

    @ManyToMany
    @JoinTable(name = "user_following",
        joinColumns = @JoinColumn(name = "following_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserProfileEntity> followers;


    // User likes:

    @ManyToMany
    @JoinTable(
        name = "posts_likes",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<PostEntity> likedPosts;

    @ManyToMany
    @JoinTable(
        name = "comments_likes",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
private List<CommentEntity> likedComments;

    public static UserProfileEntity defaultBuild() {
        return UserProfileEntity.builder()
            .firstname("default")
            .lastname("default")
            .following(new ArrayList<>())
            .userPosts(new ArrayList<>())
            .userComments(new ArrayList<>())
            .likedPosts(new ArrayList<>())
            .likedComments(new ArrayList<>())
            .build();
    }
}