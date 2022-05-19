package zaritalk.community.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zaritalk.community.app.domain.Posting;

import java.util.List;
import java.util.Optional;

public interface PostingRepository extends JpaRepository<Posting, Long> {
    Posting save(Posting posting);

    Optional<Posting> findById(Long id);

    List<Posting> findAllByOrderByCreatedAtDesc();
}
