package zaritalk.community.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zaritalk.community.app.domain.AccountTypeConvertor;
import zaritalk.community.app.domain.Like;
import zaritalk.community.app.domain.Posting;
import zaritalk.community.app.domain.User;
import zaritalk.community.app.repository.CommentRepository;
import zaritalk.community.app.repository.PostingRepository;
import zaritalk.community.app.repository.UserRepository;
import zaritalk.community.enums.EAccountType;
import zaritalk.community.enums.ELikeResult;
import zaritalk.community.exceptions.AccountTypeMismatch;
import zaritalk.community.exceptions.NotAuthorized;
import zaritalk.community.exceptions.PostingNotFound;
import zaritalk.community.exceptions.UserNotFound;
import zaritalk.community.app.repository.LikeRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostingService {

    public final LikeRepository likeRepository;
    public final CommentRepository commentRepository;

    public final UserRepository userRepository;
    public final PostingRepository postingRepository;

    @Transactional(readOnly = true)
    public List<Posting> getPostings() {
        return postingRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public Posting getPosting(Long postingId) {
        Optional<Posting> posting = postingRepository.findById(postingId);
        if (!posting.isPresent()) {
            throw new PostingNotFound("Posting Not Found");
        }

        return posting.get();
    }

    public Posting createPosting(String title, String content, Long userId, String accountTypeString) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new UserNotFound("User Not Found");
        }
        User user = userOptional.get();

        EAccountType accountType = AccountTypeConvertor.convertStringToAttribute(accountTypeString);
        if (accountType != user.getAccountType()) {
            throw new AccountTypeMismatch("Account Type Mismatch");
        }

        Posting posting = Posting.create(title, content, user);
        return postingRepository.save(posting);
    }

    public void updatePosting(String newTitle, String newContent, Long postingId, Long userId, String accountTypeString) {
        Optional<Posting> postingOptional = postingRepository.findById(postingId);
        if (!postingOptional.isPresent()) {
            throw new PostingNotFound("Posting Not Found");
        }
        Posting posting = postingOptional.get();

        if (!Objects.equals(posting.getUser().getId(), userId)) {
            throw new NotAuthorized("Not Authorized");
        }

        EAccountType accountType = AccountTypeConvertor.convertStringToAttribute(accountTypeString);
        if (accountType != posting.getUser().getAccountType()) {
            throw new AccountTypeMismatch("Account Type Mismatch");
        }

        posting.update(newTitle, newContent);
    }

    public void deletePosting(Long postingId, Long userId, String accountTypeString) {
        Optional<Posting> postingOptional = postingRepository.findById(postingId);
        if (!postingOptional.isPresent()) {
            throw new PostingNotFound("Posting Not Found");
        }
        Posting posting = postingOptional.get();

        if (!Objects.equals(posting.getUser().getId(), userId)) {
            throw new NotAuthorized("Not Authorized");
        }

        EAccountType accountType = AccountTypeConvertor.convertStringToAttribute(accountTypeString);
        if (accountType != posting.getUser().getAccountType()) {
            throw new AccountTypeMismatch("Account Type Mismatch");
        }

        posting.remove();
    }

    public ELikeResult like(Long postingId, Long userId, String accountTypeString) {
        Optional<Posting> postingOptional = postingRepository.findById(postingId);
        if (!postingOptional.isPresent()) {
            throw new PostingNotFound("Posting Not Found");
        }
        Posting posting = postingOptional.get();

        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new UserNotFound("User Not Found");
        }
        User user = userOptional.get();

        EAccountType accountType = AccountTypeConvertor.convertStringToAttribute(accountTypeString);
        if (accountType != user.getAccountType()) {
            throw new AccountTypeMismatch("Account Type Mismatch");
        }

        List<Like> likesByUser = user.getLikes();
        for (Like like : likesByUser) {
            if (like.getPosting().equals(posting)) {
                if (!like.isDeleted()) {
                    like.deactivate();
                    return ELikeResult.DELETED;
                }
            }

        }

        Like like = Like.create(posting, user);
        likeRepository.save(like);

        return ELikeResult.CREATED;
    }


}
