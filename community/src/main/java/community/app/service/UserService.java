package community.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import community.app.domain.AccountTypeConvertor;
import community.app.domain.User;
import community.enums.EAccountType;
import community.exceptions.AccountTypeNotExist;
import community.exceptions.DuplicateUser;
import community.app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User save(String accountId, String nickname, String accountTypeString) {
        List<User> users = userRepository.findAllByOrderByCreatedAtDesc();
        for (User user : users) {
            if (user.getAccountId().equals(accountId)) {
                throw new DuplicateUser("Duplicated User");
            }
        }

        EAccountType accountType = AccountTypeConvertor.convertStringToAttribute(accountTypeString);
        if (accountType == EAccountType.NONE) {
            throw new AccountTypeNotExist("Account Type Not Exist");
        }

        User user = User.create(accountId, nickname, accountType);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findOneOrNull(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAllByOrderByCreatedAtDesc();
    }
}
