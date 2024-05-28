package socialmedia.backend.user.userAuth.entity;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import socialmedia.backend.security.jwt.refreshToken.entity.RefreshTokenEntity;
import socialmedia.backend.user.userProfile.entity.UserProfileEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_auth")
public class UserAuthEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "roles")
    private String roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_profile_id")
    private UserProfileEntity userProfile;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_refresh_id")
    private RefreshTokenEntity refreshTokens;


    // Booleans needed for userDetails
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;

    // Methods needed for User Details
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays
            .stream(this.roles
                    .split(","))
            .map(SimpleGrantedAuthority::new)
            .toList();
    }

    // Userdetail's username is ours email.
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
