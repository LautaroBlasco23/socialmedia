package socialmedia.backend.user.userProfile.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<PostEntity> userPosts;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<CommentEntity> userComments;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_following",
               joinColumns = @JoinColumn(name = "follower_id"),
               inverseJoinColumns = @JoinColumn(name = "followed_id"))
    private List<UserProfileEntity> following;

    @JsonIgnore
    @ManyToMany
    private List<PostEntity> likedPosts;

    @JsonIgnore
    @ManyToMany
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

    public Long getId() {
        return this.auth.getId();
    }
}