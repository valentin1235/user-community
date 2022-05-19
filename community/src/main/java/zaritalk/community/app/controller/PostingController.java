package zaritalk.community.app.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zaritalk.community.app.domain.Comment;
import zaritalk.community.app.domain.Posting;
import zaritalk.community.app.domain.User;
import zaritalk.community.app.service.UserService;
import zaritalk.community.enums.ELikeResult;
import zaritalk.community.exceptions.AccountTypeMismatch;
import zaritalk.community.exceptions.NotAuthorized;
import zaritalk.community.exceptions.PostingNotFound;
import zaritalk.community.exceptions.UserNotFound;
import zaritalk.community.app.service.PostingService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostingController {

    private final PostingService postingService;
    private final UserService userService;

    @GetMapping("/postings")
    public ResponseEntity getPostings(@RequestAttribute("userId") Long userId) {
        List<PostingsDto> result = new ArrayList<>();
        List<Posting> postings = postingService.getPostings();
        User user = userService.findOneOrNull(userId);
        for (Posting posting : postings) {
            result.add(new PostingsDto(posting, user));
        }

        return new ResponseEntity<>(result, null, HttpStatus.OK);
    }

    @GetMapping("/postings/{postingId}/detail")
    public ResponseEntity getPosting(@RequestAttribute("userId") Long userId,
                                        @PathVariable("postingId") Long postingId) {
        try {
            User user = userService.findOneOrNull(userId);
            Posting posting = postingService.getPosting(postingId);
            PostingDetailDto result = new PostingDetailDto(posting, user);

            return new ResponseEntity<>(result, null, HttpStatus.OK);
        } catch (PostingNotFound e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/postings/posting")
    public ResponseEntity createPosting(@RequestAttribute("userId") Long userId,
                                        @RequestAttribute("accountType") String accountType,
                                        @RequestBody @Valid CreatePostingRequest body) {
        try {
            postingService.createPosting(body.getTitle(), body.getContent(), userId, accountType);

            return new ResponseEntity<>(null, null, HttpStatus.CREATED);
        } catch (UserNotFound e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        } catch (AccountTypeMismatch e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/postings/{postingId}")
    public ResponseEntity updatePosting(@RequestAttribute("userId") Long userId,
                                        @RequestAttribute("accountType") String accountType,
                                        @PathVariable("postingId") Long postingId,
                                        @RequestBody @Valid UpdatePostingRequest body) {
        try {
            postingService.updatePosting(body.getTitle(), body.getContent(), postingId, userId, accountType);

            return new ResponseEntity<>(null, null, HttpStatus.OK);
        } catch (UserNotFound | PostingNotFound e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        } catch (AccountTypeMismatch | NotAuthorized e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/postings/{postingId}")
    public ResponseEntity deletePosting(@RequestAttribute("userId") Long userId,
                                        @RequestAttribute("accountType") String accountType,
                                        @PathVariable("postingId") Long postingId) {
        try {
            postingService.deletePosting(postingId, userId, accountType);

            return new ResponseEntity<>(null, null, HttpStatus.OK);
        } catch (UserNotFound e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        } catch (AccountTypeMismatch | NotAuthorized e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/postings/{postingId}/like")
    public ResponseEntity like(@PathVariable("postingId") Long postingId,
                               @RequestAttribute("userId") Long userId,
                               @RequestAttribute("accountType") String accountType) {
        try {
            ELikeResult result = postingService.like(postingId, userId, accountType);

            return new ResponseEntity<>(result.name(), null, HttpStatus.CREATED);
        } catch (UserNotFound | PostingNotFound e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        } catch (AccountTypeMismatch e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNAUTHORIZED);
        }
    }

    // data definitions
    @Data
    static class CreatePostingRequest {
        @NotEmpty
        private String title;

        @NotEmpty
        private String content;
    }

    @Data
    static class UpdatePostingRequest {
        private String title;
        private String content;
    }

    @Data
    static class CommentDto {
        private Long id;
        private String content;
        private Long userId;
        private String displayUserName;
        private LocalDateTime createdAt;

        CommentDto(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.userId = comment.getUser().getId();
            this.displayUserName = comment.getDisplayName();
            this.createdAt = comment.getCreatedAt();
        }
    }

    @Data
    static class PostingDetailDto {
        private Long id;
        private String title;
        private String content;
        private Long userId;
        private String displayUserName;
        private int likeCount;
        private LocalDateTime createdAt;
        private boolean isLiked;
        private List<CommentDto> comments = new ArrayList<>();

        PostingDetailDto(Posting posting, User userOrNull) {
            this.id = posting.getId();
            this.title = posting.getTitle();
            this.content = posting.getContent();
            this.userId = posting.getUser().getId();
            this.createdAt = posting.getCreatedAt();

            this.displayUserName = posting.getDisplayName();
            this.likeCount = posting.getLikeCount();
            this.isLiked = posting.isLikedBy(userOrNull);

            List<Comment> comments = posting.getComments();
            for (Comment comment : comments) {
                this.comments.add(new CommentDto(comment));
            }
        }
    }

    @Data
    static class PostingsDto {
        private Long id;
        private String title;
        private String content;
        private Long userId;
        private String displayUserName;
        private int likeCount;
        private LocalDateTime createdAt;
        private boolean isLiked;

        PostingsDto(Posting posting, User userOrNull) {
            this.id = posting.getId();
            this.title = posting.getTitle();
            this.content = posting.getContent();
            this.userId = posting.getUser().getId();
            this.createdAt = posting.getCreatedAt();

            this.displayUserName = posting.getDisplayName();
            this.likeCount = posting.getLikeCount();
            this.isLiked = posting.isLikedBy(userOrNull);
        }
    }
}
