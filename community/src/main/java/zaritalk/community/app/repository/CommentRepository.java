package zaritalk.community.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zaritalk.community.app.domain.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);
}
