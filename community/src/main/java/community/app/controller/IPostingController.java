package community.app.controller;

import community.searches.PostingSearch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

public interface IPostingController {
    @GetMapping("/postings")
    public ResponseEntity getPostings(@RequestAttribute("userId") Long userId,
                                      @ModelAttribute("postingSearch") PostingSearch postingSearch,
                                      @RequestParam(value = "offset", defaultValue = "0") int offset,
                                      @RequestParam(value = "limit", defaultValue = "1000") int limit);

    @GetMapping("/postings/{postingId}/detail")
    public ResponseEntity getPosting(@RequestAttribute("userId") Long userId,
                                     @PathVariable("postingId") Long postingId);

    @PostMapping("/postings/posting")
    public ResponseEntity createPosting(@RequestAttribute("userId") Long userId,
                                        @RequestAttribute("accountType") String accountType,
                                        @RequestBody @Valid PostingController.CreatePostingRequest body);

    @PutMapping("/postings/{postingId}")
    public ResponseEntity updatePosting(@RequestAttribute("userId") Long userId,
                                        @RequestAttribute("accountType") String accountType,
                                        @PathVariable("postingId") Long postingId,
                                        @RequestBody @Valid PostingController.UpdatePostingRequest body);

    @DeleteMapping("/postings/{postingId}")
    public ResponseEntity deletePosting(@RequestAttribute("userId") Long userId,
                                        @RequestAttribute("accountType") String accountType,
                                        @PathVariable("postingId") Long postingId);

    @PostMapping("/postings/{postingId}/like")
    public ResponseEntity like(@PathVariable("postingId") Long postingId,
                               @RequestAttribute("userId") Long userId,
                               @RequestAttribute("accountType") String accountType);
}
