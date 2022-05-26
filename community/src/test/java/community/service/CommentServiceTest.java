package community.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import community.app.domain.Comment;
import community.app.domain.Posting;
import community.app.domain.User;
import community.app.repository.CommentRepository;
import community.app.service.CommentService;
import community.enums.EAccountType;
import community.exceptions.AccountTypeMismatch;
import community.exceptions.CommentNotFound;
import community.exceptions.NotAuthorized;
import community.exceptions.PostingNotFound;
import community.exceptions.UserNotFound;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 성공_댓글_생성() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        Comment comment = commentService.createComment("content", posting.getId(), user1.getId(), "Lessor");

        // then
        assertThat(comment).isEqualTo(commentRepository.findById(comment.getId()).get());
    }

    @Test
    public void 실패_댓글_생성_유저타입불일치() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        // when
        Throwable thrown = assertThrows(
                AccountTypeMismatch.class,
                () -> commentService.createComment("content", posting.getId(), user1.getId(), "Lessee")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Account Type Mismatch");
    }

    @Test
    public void 실패_댓글_생성_유저찾을수없음() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        // when
        Throwable thrown = assertThrows(
                UserNotFound.class,
                () -> commentService.createComment("content", posting.getId(), 100000L, "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("User Not Found");
    }

    @Test
    public void 실패_댓글_생성_게시글찾을수없음() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        // when
        Throwable thrown = assertThrows(
                PostingNotFound.class,
                () -> commentService.createComment("content", 10000L, user1.getId(), "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Posting Not Found");
    }

    @Test
    public void 성공_댓글_수정() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        // when
        commentService.updateComment("newContent", comment.getId(), user1.getId(), "Lessor");

        // then
        Comment updatedComment = commentRepository.findById(comment.getId()).get();
        assertThat(updatedComment).isNotNull();
        assertThat(updatedComment.getContent()).isEqualTo("newContent");
    }

    @Test
    public void 실패_댓글_수정_유저찾을수없음() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        // when
        Throwable thrown = assertThrows(
                UserNotFound.class,
                () -> commentService.updateComment("newContent", comment.getId(), 10000L, "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("User Not Found");
    }

    @Test
    public void 실패_댓글_수정_댓글찾을수없음() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        // when
        Throwable thrown = assertThrows(
                CommentNotFound.class,
                () -> commentService.updateComment("newContent", 10000L, user1.getId(), "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Comment Not Found");
    }

    @Test
    public void 실패_댓글_수정_권한없음() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        User user2 = User.create("account2", "name2", EAccountType.LESSOR);
        em.persist(user2);

        // when
        Throwable thrown = assertThrows(
                NotAuthorized.class,
                () -> commentService.updateComment("newContent", comment.getId(), user2.getId(), "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Not Authorized");
    }

    @Test
    public void 실패_댓글_수정_유저타입불일치() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        // when
        Throwable thrown = assertThrows(
                AccountTypeMismatch.class,
                () -> commentService.updateComment("newContent", comment.getId(), user1.getId(), "Lessee")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Account Type Mismatch");
    }

    @Test
    public void 성공_댓글_삭제() {
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        // when
        commentService.deleteComment(comment.getId(), user1.getId(), "Lessor");

        // then
        Comment deletedComment = commentRepository.findById(comment.getId()).get();
        assertThat(deletedComment.isDeleted()).isEqualTo(true);
    }

    @Test
    public void 실패_댓글_삭제_유저찾을수없음() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        // when
        Throwable thrown = assertThrows(
                UserNotFound.class,
                () -> commentService.deleteComment(comment.getId(), 100000L, "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("User Not Found");
    }

    @Test
    public void 실패_댓글_삭제_댓글찾을수없음() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        // when
        Throwable thrown = assertThrows(
                CommentNotFound.class,
                () -> commentService.deleteComment(10000L, user1.getId(), "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Comment Not Found");
    }

    @Test
    public void 실패_댓글_삭제_권한없음() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        User user2 = User.create("account2", "name2", EAccountType.LESSOR);
        em.persist(user2);

        // when
        Throwable thrown = assertThrows(
                NotAuthorized.class,
                () -> commentService.deleteComment(comment.getId(), user2.getId(), "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Not Authorized");
    }

    @Test
    public void 실패_댓글_삭제_유저타입불일치() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);
        Comment comment = Comment.create("content", posting, user1);
        em.persist(comment);

        // when
        Throwable thrown = assertThrows(
                AccountTypeMismatch.class,
                () -> commentService.deleteComment(comment.getId(), user1.getId(), "Lessee")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Account Type Mismatch");
    }
}