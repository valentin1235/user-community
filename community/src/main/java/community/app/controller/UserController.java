package community.app.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import community.app.domain.User;
import community.exceptions.AccountTypeNotExist;
import community.exceptions.DuplicateUser;
import community.app.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/user")
    public ResponseEntity createUser(@RequestBody @Valid CreateUserRequest body) {
        try {
            userService.save(body.getAccountId(), body.getNickname(), body.getAccountType());
            return new ResponseEntity<>(null, null, HttpStatus.CREATED);
        } catch (AccountTypeNotExist | DuplicateUser e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users")
    public ResponseEntity getAllUser() {
        final List<UsersDto> result = new ArrayList<>();
        final List<User> users = userService.findAll();
        for (User user : users) {
            result.add(new UsersDto(user));
        }

        return new ResponseEntity<>(result, null, HttpStatus.OK);
    }

    @Data
    static class CreateUserRequest {
        @NotEmpty
        private String accountId;

        @NotEmpty
        private String nickname;

        @NotEmpty
        private String accountType;
    }

    @Data
    static class UsersDto {
        private Long id;
        private String accountId;
        private String nickname;
        private String accountType;
        private LocalDateTime createdAt;

        UsersDto(User user) {
            this.id = user.getId();
            this.accountId = user.getAccountId();
            this.nickname = user.getNickname();
            this.accountType = user.getAccountType().toString();
            this.createdAt = user.getCreatedAt();
        }
    }
}
