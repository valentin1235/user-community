package community.app.controller;

import community.dtos.PostingDetailDto;
import community.dtos.PostingsDto;
import community.searches.PostingSearch;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import community.enums.ELikeResult;
import community.exceptions.AccountTypeMismatch;
import community.exceptions.NotAuthorized;
import community.exceptions.PostingNotFound;
import community.exceptions.UserNotFound;
import community.app.service.PostingService;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RequestMapping
@ResponseBody
public class PostingController implements IPostingController {

    private final PostingService postingService;

    public PostingController(PostingService postingService) {
        this.postingService = postingService;
    }


    @GetMapping("/postings")
    public ResponseEntity getPostings(@RequestAttribute("userId") Long userId,
                                      @ModelAttribute("postingSearch") PostingSearch postingSearch,
                                      @RequestParam(value = "offset", defaultValue = "0") int offset,
                                      @RequestParam(value = "limit", defaultValue = "1000") int limit) {

        List<PostingsDto> result = postingService.getPostings(postingSearch, userId, offset, limit);

        return new ResponseEntity<>(result, null, HttpStatus.OK);
    }

    @GetMapping("/postings/{postingId}/detail")
    public ResponseEntity getPosting(@RequestAttribute("userId") Long userId,
                                        @PathVariable("postingId") Long postingId) {
        try {
            PostingDetailDto result = postingService.getPosting(postingId, userId);

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
        } catch (PostingNotFound e) {
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
}
