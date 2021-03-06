package community.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import community.app.domain.AccountTypeConvertor;
import community.app.domain.Comment;
import community.app.domain.Posting;
import community.app.domain.User;
import community.app.repository.CommentRepository;
import community.app.repository.PostingRepository;
import community.app.repository.UserRepository;
import community.enums.EAccountType;
import community.exception.AccountTypeMismatch;
import community.exception.CommentNotFound;
import community.exception.NotAuthorized;
import community.exception.PostingNotFound;
import community.exception.UserNotFound;

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
         * @TODO UserNotFound, NotAuthorized, AccountTypeMismatch ?????????????????? ????????????
         */
        EAccountType accountType = AccountTypeConvertor.convertStringToAttribute(accountTypeString);
        if (accountType != comment.getUser().getAccountType()) {
            throw new AccountTypeMismatch("Account Type Mismatch");
        }

        comment.remove();
    }
}
