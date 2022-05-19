package zaritalk.community.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zaritalk.community.app.controller.domain.Posting;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostingRepository {

    private final EntityManager em;

    public void save(Posting posting) {
        this.em.persist(posting);
    }

    public Posting findOneOrNull(Long postingId) {
        final Posting posting = em.find(Posting.class, postingId);

        return posting;
    }

    public List<Posting> findAll() {
        return this.em.createQuery("select p from Posting p where p.isDeleted = false", Posting.class)
                .getResultList();
    }
}
