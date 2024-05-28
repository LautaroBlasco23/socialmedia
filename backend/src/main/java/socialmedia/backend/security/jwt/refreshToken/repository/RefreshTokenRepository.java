package socialmedia.backend.security.jwt.refreshToken.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import socialmedia.backend.security.jwt.refreshToken.entity.RefreshTokenEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM refresh_tokens WHERE user_id = ?1", nativeQuery = true)
    void deleteTokensByUserId(Long userId);
}
