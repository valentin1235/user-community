package zaritalk.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zaritalk.community.app.controller.domain.AccountTypeConvertor;
import zaritalk.community.app.controller.domain.User;
import zaritalk.community.enums.EAccountType;
import zaritalk.community.exceptions.AccountTypeNotExist;
import zaritalk.community.exceptions.DuplicateUser;
import zaritalk.community.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public void save(String accountId, String nickname, String accountTypeString) {
        List<User> users = this.findAll();
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
        userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
