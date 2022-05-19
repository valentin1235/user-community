package zaritalk.community.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zaritalk.community.app.domain.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like save(Like like);
}
