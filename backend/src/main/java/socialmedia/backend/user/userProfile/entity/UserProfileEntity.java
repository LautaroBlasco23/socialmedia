package socialmedia.backend.user.userProfile.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_id",referencedColumnName = "id")
    private UserAuthEntity auth;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @ElementCollection
    private List<Long> following;

    @ElementCollection
    @Column(name = "user_posts")
    private List<Long> userPosts;

    @Column(name = "liked_posts")
    private List<Long> likedPosts;

    @ElementCollection
    @Column(name = "liked_comments")
    private List<Long> likedComments;

    @ElementCollection
    @Column(name = "user_comments")
    private List<Long> userComments;

    public static UserProfileEntity defaultBuild() {
        return UserProfileEntity.builder()
            .firstname("default")
            .lastname("default")
            .following(new ArrayList<Long>())
            .userPosts(new ArrayList<Long>())
            .userComments(new ArrayList<Long>())
            .likedPosts(new ArrayList<Long>())
            .likedComments(new ArrayList<Long>())
            .build();
    }

    public Long getId() {
        return this.auth.getId();
    }
}