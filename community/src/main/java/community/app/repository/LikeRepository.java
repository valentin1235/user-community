package community.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import community.app.domain.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like save(Like like);
}
