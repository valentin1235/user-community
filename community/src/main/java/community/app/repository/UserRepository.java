package community.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import community.app.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByOrderByCreatedAtDesc();
}
