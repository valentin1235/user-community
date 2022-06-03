package community.app.service;

import com.querydsl.core.Tuple;
import community.app.repository.query.CommentQueryRepository;
import community.app.repository.query.PostingQueryRepository;
import community.dtos.CommentDto;
import community.dtos.PostingDetailDto;
import community.dtos.PostingsDto;
import community.searches.PostingSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import community.app.domain.AccountTypeConvertor;
import community.app.domain.Like;
import community.app.domain.Posting;
import community.app.domain.User;
import community.app.repository.CommentRepository;
import community.app.repository.PostingRepository;
import community.app.repository.UserRepository;
import community.enums.EAccountType;
import community.enums.ELikeResult;
import community.exceptions.AccountTypeMismatch;
import community.exceptions.NotAuthorized;
import community.exceptions.PostingNotFound;
import community.exceptions.UserNotFound;
import community.app.repository.LikeRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostingService {

    public final LikeRepository likeRepository;
    public final CommentRepository commentRepository;

    public final UserRepository userRepository;
    public final PostingRepository postingRepository;
    public final PostingQueryRepository postingQueryRepository;
    public final CommentQueryRepository commentQueryRepository;


    @Transactional(readOnly = true)
    public List<PostingsDto> getPostings(PostingSearch postingSearch, Long userId, int offset, int limit) {
        List<PostingsDto> postingsDtos = postingQueryRepository.findAll(postingSearch, offset, limit);

        List<Long> ids = postingsDtos.stream().map(PostingsDto::getId).collect(Collectors.toList());
        List<Tuple> likedList = postingQueryRepository.findLikedList(ids, userId);

        Map<Long, List<PostingsDto>> map = postingsDtos.stream().collect(Collectors.groupingBy(PostingsDto::getId));
        likedList.forEach(o -> {
            map.get(o.get(0, Long.class))
                .get(0)
                .setLiked(Boolean.TRUE.equals(o.get(1, Boolean.class)));
        });

        return postingsDtos;
    }

    @Transactional(readOnly = true)
    public PostingDetailDto getPosting(Long postingId, Long userId) {
        PostingDetailDto postingDto = postingQueryRepository.findOne(postingId);
        if (postingDto == null) {
            throw new PostingNotFound("Posting Not Found");
        }

        // set liked
        Tuple liked = postingQueryRepository.findLiked(postingId, userId);
        postingDto.setLiked(Boolean.TRUE.equals(liked.get(1, Boolean.class)));

        // set comment
        List<CommentDto> commentDtos = commentQueryRepository.findBy(postingId);
        postingDto.setComments(commentDtos);

        return postingDto;
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
