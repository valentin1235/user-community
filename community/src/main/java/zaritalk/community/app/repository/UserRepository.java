package zaritalk.community.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zaritalk.community.app.domain.Posting;
import zaritalk.community.app.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User save(User posting);

    Optional<User> findById(Long id);

    List<User> findAllByOrderByCreatedAtDesc();
}
