package app.javacode.repository;

import app.javacode.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Modifying
    @Query("UPDATE User u SET u.failedAttempt = :failAttempts WHERE u.username = :username")
    void updateFailedAttempts(@Param("failAttempts") int failAttempts,
                              @Param("username") String username);
}
