package zaritalk.community.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zaritalk.community.app.controller.domain.User;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public void save(User user) {
        this.em.persist(user);
    }

    public User findOneOrNull(Long userId) {
        final User user = em.find(User.class, userId);

        return user;
    }

    public List<User> findAll() {
        return this.em.createQuery("select u from User u where u.quit = false", User.class)
                .getResultList();
    }
}
