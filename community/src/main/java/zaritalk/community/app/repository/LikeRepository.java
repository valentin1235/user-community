package zaritalk.community.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zaritalk.community.app.controller.domain.Like;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class LikeRepository {

    private final EntityManager em;

    public void save(Like like) {
        this.em.persist(like);
    }

    public Like findOne(Long likeId) {
        return this.em.find(Like.class, likeId);
    }
}
