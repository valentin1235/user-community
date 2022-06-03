package community.service;

import com.querydsl.core.Tuple;
import community.app.repository.query.PostingQueryRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import community.app.domain.Posting;
import community.app.domain.User;
import community.app.repository.PostingRepository;
import community.app.repository.UserRepository;
import community.app.service.PostingService;
import community.enums.EAccountType;
import community.enums.ELikeResult;
import community.exceptions.AccountTypeMismatch;
import community.exceptions.NotAuthorized;
import community.exceptions.PostingNotFound;
import community.exceptions.UserNotFound;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostingServiceTest {

    @Autowired
    PostingService postingService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostingRepository postingRepository;
    @Autowired
    PostingQueryRepository postingQueryRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 성공_게시글_생성() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);

        // when
        Posting posting = postingService.createPosting("title", "content", user1.getId(), "Lessor");

        // then
        assertThat(posting).isEqualTo(postingRepository.findById(posting.getId()).get());
    }

    @Test
    public void 실패_게시글_생성_유저타입불일치() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);

        // when
        Throwable thrown = assertThrows(
                AccountTypeMismatch.class,
                () -> postingService.createPosting("title", "content", user1.getId(), "Lessee")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Account Type Mismatch");
    }

    @Test
    public void 실패_게시글_생성_유저찾을수없음() throws Exception {
        // given

        // when
        Throwable thrown = assertThrows(
                UserNotFound.class,
                () -> postingService.createPosting("title", "content", 100000L, "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("User Not Found");
    }

    @Test
    public void 성공_게시글_수정() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        postingService.updatePosting("newTitle", "newContent", posting.getId(), user1.getId(), "Lessor");

        // then
        Posting updatedPosting = postingRepository.findById(posting.getId()).get();
        assertThat(updatedPosting).isNotNull();
        assertThat(updatedPosting.getTitle()).isEqualTo("newTitle");
        assertThat(updatedPosting.getContent()).isEqualTo("newContent");
    }

    @Test
    public void 실패_게시글_수정_게시글찾을수없음() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        Throwable thrown = assertThrows(
                PostingNotFound.class,
                () -> postingService.updatePosting("newTitle", "newContent", 10000L, user1.getId(), "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Posting Not Found");
    }

    @Test
    public void 실패_게시글_수정_권한없음() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        User user2 = User.create("account2", "name2", EAccountType.LESSOR);
        userRepository.save(user2);

        // when
        Throwable thrown = assertThrows(
                NotAuthorized.class,
                () -> postingService.updatePosting("newTitle", "newContent", posting.getId(), user2.getId(), "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Not Authorized");
    }

    @Test
    public void 실패_게시글_수정_유저타입불일치() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        Throwable thrown = assertThrows(
                AccountTypeMismatch.class,
                () -> postingService.updatePosting("newTitle", "newContent", posting.getId(), user1.getId(), "Lessee")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Account Type Mismatch");
    }

    @Test
    public void 성공_게시글_삭제() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        postingService.deletePosting(posting.getId(), user1.getId(), "Lessor");

        // then
        Optional<Posting> deletedPosting = postingRepository.findById(posting.getId());
        assertThat(deletedPosting.get().isDeleted()).isEqualTo(true);
    }

    @Test
    public void 실패_게시글_삭제_게시글찾을수없음() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        Throwable thrown = assertThrows(
                PostingNotFound.class,
                () -> postingService.deletePosting(10000L, user1.getId(), "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Posting Not Found");
    }

    @Test
    public void 실패_게시글_삭제_권한없음() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        User user2 = User.create("account2", "name2", EAccountType.LESSOR);
        em.persist(user2);

        // when
        Throwable thrown = assertThrows(
                NotAuthorized.class,
                () -> postingService.deletePosting(posting.getId(), user2.getId(), "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Not Authorized");
    }

    @Test
    public void 실패_게시글_삭제_유저타입불일치() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        Throwable thrown = assertThrows(
                AccountTypeMismatch.class,
                () -> postingService.deletePosting(posting.getId(), user1.getId(), "Lessee")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Account Type Mismatch");
    }



    @Test
    public void 성공_좋아요_생성() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        ELikeResult result = postingService.like(posting.getId(), user1.getId(), "Lessor");

        // then
        assertThat(result).isEqualTo(ELikeResult.CREATED);
        assertThat(posting.getLikes().size()).isEqualTo(1);
    }

    @Test
    public void 성공_좋아요_삭제() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        postingService.like(posting.getId(), user1.getId(), "Lessor");
        ELikeResult result = postingService.like(posting.getId(), user1.getId(), "Lessor");

        // then
        assertThat(result).isEqualTo(ELikeResult.DELETED);
        assertThat(posting.getLikes().size()).isEqualTo(0);
    }

    @Test
    public void 성공_좋아요_지웠다가생성() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        postingService.like(posting.getId(), user1.getId(), "Lessor");
        postingService.like(posting.getId(), user1.getId(), "Lessor");
        ELikeResult result = postingService.like(posting.getId(), user1.getId(), "Lessor");

        // then
        assertThat(result).isEqualTo(ELikeResult.CREATED);
        assertThat(posting.getLikes().size()).isEqualTo(1);
    }

    @Test
    public void 성공_좋아요_좋아요여부() {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting savedPosting = Posting.create("title1", "content1", user1);
        em.persist(savedPosting);

        User user2 = User.create("account2", "name2", EAccountType.LESSOR);
        em.persist(user2);

        // when
        postingService.like(savedPosting.getId(), user1.getId(), "Lessor");

        // then
        Tuple user1Liked = postingQueryRepository.findLiked(savedPosting.getId(), user1.getId());
        Tuple user2Liked = postingQueryRepository.findLiked(savedPosting.getId(), user2.getId());
        assertThat(user1Liked.get(1, Boolean.class)).isEqualTo(true);
        assertThat(user2Liked.get(1, Boolean.class)).isEqualTo(false);
    }

    @Test
    public void 실패_좋아요_유저타입불일치() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        Throwable thrown = assertThrows(
                AccountTypeMismatch.class,
                () -> postingService.like(posting.getId(), user1.getId(), "Lessee")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Account Type Mismatch");
    }

    @Test
    public void 실패_좋아요_유저찾을수없음() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        Throwable thrown = assertThrows(
                UserNotFound.class,
                () -> postingService.like(posting.getId(), 100000L, "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("User Not Found");
    }

    @Test
    public void 실패_좋아요_게시글찾을수없음() throws Exception {
        // given
        User user1 = User.create("account1", "name1", EAccountType.LESSOR);
        em.persist(user1);
        Posting posting = Posting.create("title1", "content1", user1);
        em.persist(posting);

        // when
        Throwable thrown = assertThrows(
                PostingNotFound.class,
                () -> postingService.like(10000L, user1.getId(), "Lessor")
        );

        // then
        assertThat(thrown.getMessage()).isEqualTo("Posting Not Found");
    }
}