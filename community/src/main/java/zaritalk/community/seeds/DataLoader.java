package zaritalk.community.seeds;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import zaritalk.community.app.domain.Posting;
import zaritalk.community.app.domain.User;
import zaritalk.community.app.repository.PostingRepository;
import zaritalk.community.app.repository.UserRepository;
import zaritalk.community.enums.EAccountType;

/*
 * @TODO Deactivate this data loader at the production environment!
 */

@Component
@RequiredArgsConstructor
@Transactional
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostingRepository postingRepository;

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        User user2 = User.create("account2", "name2", EAccountType.LESSOR);
        User user3 = User.create("account3", "name3", EAccountType.LESSOR);
        User user4 = User.create("account4", "name4", EAccountType.LESSOR);
        User user5 = User.create("account5", "name5", EAccountType.LESSOR);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        postingRepository.save(Posting.create("title1", "content1", user1));
        postingRepository.save(Posting.create("title2", "content2", user2));
    }
}
