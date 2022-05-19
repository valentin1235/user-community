package zaritalk.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zaritalk.community.app.controller.domain.AccountTypeConvertor;
import zaritalk.community.app.controller.domain.Like;
import zaritalk.community.app.controller.domain.Posting;
import zaritalk.community.app.controller.domain.User;
import zaritalk.community.enums.EAccountType;
import zaritalk.community.enums.ELikeResult;
import zaritalk.community.exceptions.AccountTypeMismatch;
import zaritalk.community.exceptions.NotAuthorized;
import zaritalk.community.exceptions.PostingNotFound;
import zaritalk.community.exceptions.UserNotFound;
import zaritalk.community.repository.LikeRepository;
import zaritalk.community.repository.PostingRepository;
import zaritalk.community.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PostingService {

    public final UserRepository userRepository;
    public final PostingRepository postingRepository;
    public final LikeRepository likeRepository;

    public List<Posting> getPostings() {
        return postingRepository.findAll();
    }

    public void createPosting(String title, String content, Long userId, String accountTypeString) {
        User user = userRepository.findOneOrNull(userId);
        if (user == null || user.isQuit()) {
            throw new UserNotFound("User Not Found");
        }

        EAccountType accountType = AccountTypeConvertor.convertStringToAttribute(accountTypeString);
        if (accountType != user.getAccountType()) {
            throw new AccountTypeMismatch("Account Type Mismatch");
        }

        Posting posting = Posting.create(title, content, user);
        postingRepository.save(posting);
    }

    public void updatePosting(String newTitle, String newContent, Long postingId, Long userId, String accountTypeString) {
        Posting posting = postingRepository.findOneOrNull(postingId);
        if (posting == null || posting.isDeleted()) {
            throw new PostingNotFound("Posting Not Found");
        }

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
        Posting posting = postingRepository.findOneOrNull(postingId);
        if (posting == null || posting.isDeleted()) {
            throw new PostingNotFound("Posting Not Found");
        }

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
        Posting posting = postingRepository.findOneOrNull(postingId);
        if (posting == null || posting.isDeleted()) {
            throw new PostingNotFound("Posting Not Found");
        }

        User user = userRepository.findOneOrNull(userId);
        if (user == null || user.isQuit()) {
            throw new UserNotFound("User Not Found");
        }

        EAccountType accountType = AccountTypeConvertor.convertStringToAttribute(accountTypeString);
        if (accountType != posting.getUser().getAccountType()) {
            throw new AccountTypeMismatch("Account Type Mismatch");
        }

        List<Like> likesByUser = user.getLikes();
        for (Like like : likesByUser) {
            if (like.getPosting().equals(posting)) {
                if (!like.isDeleted()) {
                    like.deactivate();
                    return ELikeResult.DEACTIVATED;
                } else {
                    like.activate();
                    return ELikeResult.ACTIVATED;
                }
            }

        }

        Like.create(posting, user);

        return ELikeResult.CREATED;
    }
}
