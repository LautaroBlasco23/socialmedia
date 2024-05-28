package socialmedia.backend.user.userAuth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import socialmedia.backend.user.userAuth.entity.UserAuthEntity;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuthEntity, Long> {
    public Optional<UserAuthEntity> findByEmail(String email);
}
