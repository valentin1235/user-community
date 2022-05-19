package zaritalk.community.app.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zaritalk.community.app.service.CommentService;
import zaritalk.community.app.service.PostingService;
import zaritalk.community.exceptions.AccountTypeMismatch;
import zaritalk.community.exceptions.CommentNotFound;
import zaritalk.community.exceptions.NotAuthorized;
import zaritalk.community.exceptions.PostingNotFound;
import zaritalk.community.exceptions.UserNotFound;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class CommentController {
    final private CommentService commentService;

    @PostMapping("/comments/comment")
    public ResponseEntity createComment(@RequestAttribute("userId") Long userId,
                                        @RequestAttribute("accountType") String accountType,
                                        @RequestParam("postingId") Long postingId,
                                        @RequestBody @Valid CreateCommentRequest body) {
        try {
            commentService.createComment(body.getContent(), postingId, userId, accountType);

            return new ResponseEntity<>(null, null, HttpStatus.CREATED);
        } catch (UserNotFound | PostingNotFound e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        } catch (AccountTypeMismatch e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity updateComment(@RequestAttribute("userId") Long userId,
                                        @RequestAttribute("accountType") String accountType,
                                        @PathVariable("commentId") Long commentId,
                                        @RequestBody @Valid UpdateCommentRequest body) {
        try {
            commentService.updateComment(body.getContent(), commentId, userId, accountType);

            return new ResponseEntity<>(null, null, HttpStatus.OK);
        } catch (UserNotFound | CommentNotFound e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        } catch (AccountTypeMismatch | NotAuthorized e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity deleteComment(@RequestAttribute("userId") Long userId,
                                        @RequestAttribute("accountType") String accountType,
                                        @PathVariable("commentId") Long commentId) {
        try {
            commentService.deleteComment(commentId, userId, accountType);

            return new ResponseEntity<>(null, null, HttpStatus.OK);
        } catch (UserNotFound | CommentNotFound e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.NOT_FOUND);
        } catch (AccountTypeMismatch | NotAuthorized e) {
            return new ResponseEntity<>(e.getMessage(), null, HttpStatus.UNAUTHORIZED);
        }
    }

    @Data
    static class CreateCommentRequest {

        @NotEmpty
        private String content;
    }

    @Data
    static class UpdateCommentRequest {

        @NotEmpty
        private String content;
    }
}
