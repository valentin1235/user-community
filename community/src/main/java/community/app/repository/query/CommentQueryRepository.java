package community.app.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import community.app.domain.QComment;
import community.app.domain.QLike;
import community.app.domain.QPosting;
import community.app.domain.QUser;
import community.dto.CommentDto;
import community.dto.QCommentDto;
import community.dto.QPostingDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {
    private final EntityManager em;
    public List<CommentDto> findBy(Long postingId) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        return query
                .select(new QCommentDto(
                        comment.id,
                        comment.content,
                        user.id,
                        user.nickname,
                        user.accountType,
                        comment.createdAt)
                )
                .from(comment)
                .where(comment.posting.id.eq(postingId))
                .leftJoin(comment.user, user)
                .orderBy(comment.createdAt.desc())
                .fetch();

    }
}
