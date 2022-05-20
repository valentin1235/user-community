package zaritalk.community.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zaritalk.community.app.domain.AccountTypeConvertor;
import zaritalk.community.app.domain.Comment;
import zaritalk.community.app.domain.Posting;
import zaritalk.community.app.domain.User;
import zaritalk.community.app.repository.CommentRepository;
import zaritalk.community.app.repository.PostingRepository;
import zaritalk.community.app.repository.UserRepository;
import zaritalk.community.enums.EAccountType;
import zaritalk.community.exceptions.AccountTypeMismatch;
import zaritalk.community.exceptions.CommentNotFound;
import zaritalk.community.exceptions.NotAuthorized;
import zaritalk.community.exceptions.PostingNotFound;
import zaritalk.community.exceptions.UserNotFound;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final PostingRepository postingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public Comment createComment(String content, Long postingId, Long userId, String accountTypeString) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new UserNotFound("User Not Found");
        }
        User user = userOptional.get();

        Optional<Posting> postingOptional = postingRepository.findById(postingId);
        if (!postingOptional.isPresent()) {
            throw new PostingNotFound("Posting Not Found");
        }
        Posting posting = postingOptional.get();

        EAccountType accountType = AccountTypeConvertor.convertStringToAttribute(accountTypeString);
        if (accountType != posting.getUser().getAccountType()) {
            throw new AccountTypeMismatch("Account Type Mismatch");
        }

        Comment comment = Comment.create(content, posting, user);
        return commentRepository.save(comment);
    }

    public void updateComment(String newContent, Long commentId, Long userId, String accountTypeString) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new UserNotFound("User Not Found");
        }

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (!commentOptional.isPresent()) {
            throw new CommentNotFound("Comment Not Found");
        }
        Comment comment = commentOptional.get();

        if (!Objects.equals(comment.getUser().getId(), userId)) {
            throw new NotAuthorized("Not Authorized");
        }

        EAccountType accountType = AccountTypeConvertor.convertStringToAttribute(accountTypeString);
        if (accountType != comment.getUser().getAccountType()) {
            throw new AccountTypeMismatch("Account Type Mismatch");
        }

        comment.update(newContent);
    }

    public void deleteComment(Long commentId, Long userId, String accountTypeString) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new UserNotFound("User Not Found");
        }

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (!commentOptional.isPresent()) {
            throw new CommentNotFound("Comment Not Found");
        }
        Comment comment = commentOptional.get();

        if (!Objects.equals(comment.getUser().getId(), userId)) {
            throw new NotAuthorized("Not Authorized");
        }

        /*
         * @TODO UserNotFound, NotAuthorized, AccountTypeMismatch 모듈화시켜서 중복제거
         */
        EAccountType accountType = AccountTypeConvertor.convertStringToAttribute(accountTypeString);
        if (accountType != comment.getUser().getAccountType()) {
            throw new AccountTypeMismatch("Account Type Mismatch");
        }

        comment.remove();
    }
}
