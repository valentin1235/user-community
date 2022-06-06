package community.app.controller;

import community.searches.PostingSearch;
import community.trace.TraceStatus;
import community.trace.logtrace.LogTracer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostingProxyController implements IPostingController {

    private final PostingController target;
    private final LogTracer logTracer;

    @Override
    public ResponseEntity getPostings(Long userId, PostingSearch postingSearch, int offset, int limit) {
        TraceStatus status = null;
        try {
            status = logTracer.begin("  @@@@@@@@@@@@@@  " + target.getClass().getName() + ".getPostings");
            ResponseEntity result = target.getPostings(userId, postingSearch, offset, limit);
            logTracer.end(status);
            return result;
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity getPosting(Long userId, Long postingId) {
        TraceStatus status = null;
        try {
            status = logTracer.begin("  @@@@@@@@@@@@@@  " + target.getClass().getName() + ".getPosting");
            ResponseEntity result = target.getPosting(userId, postingId);
            logTracer.end(status);
            return result;
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity createPosting(Long userId, String accountType, PostingController.CreatePostingRequest body) {
        TraceStatus status = null;
        try {
            status = logTracer.begin("  @@@@@@@@@@@@@@  " + target.getClass().getName() + ".createPosting");
            ResponseEntity result = target.createPosting(userId, accountType, body);
            logTracer.end(status);
            return result;
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity updatePosting(Long userId, String accountType, Long postingId, PostingController.UpdatePostingRequest body) {
        TraceStatus status = null;
        try {
            status = logTracer.begin("  @@@@@@@@@@@@@@  " + target.getClass().getName() + ".updatePosting");
            ResponseEntity result = target.updatePosting(userId, accountType, postingId, body);
            logTracer.end(status);
            return result;
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity deletePosting(Long userId, String accountType, Long postingId) {
        TraceStatus status = null;
        try {
            status = logTracer.begin("  @@@@@@@@@@@@@@  " + target.getClass().getName() + ".deletePosting");
            ResponseEntity result = target.deletePosting(userId, accountType, postingId);
            logTracer.end(status);
            return result;
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }

    @Override
    public ResponseEntity like(Long postingId, Long userId, String accountType) {
        TraceStatus status = null;
        try {
            status = logTracer.begin("  @@@@@@@@@@@@@@  " + target.getClass().getName() + ".like");
            ResponseEntity result = target.like(postingId, userId, accountType);
            logTracer.end(status);
            return result;
        } catch (Exception e) {
            logTracer.exception(status, e);
            throw e;
        }
    }
}
