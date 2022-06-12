package community.app.repository.query;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import community.app.domain.QLike;
import community.app.domain.QPosting;
import community.app.domain.QUser;
import community.dto.PostingDetailDto;
import community.dto.PostingsDto;
import community.dto.QPostingDetailDto;
import community.dto.QPostingsDto;
import community.searchkey.PostingSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostingQueryRepository {
    private final EntityManager em;

    public List<Tuple> findLikedList(List<Long> postingIds, Long userId) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QPosting posting = QPosting.posting;
        QLike like = QLike.like;

        return query
                .select(posting.id,
                        new CaseBuilder()
                            .when(like.id.count().eq(0L))
                            .then(false)
                            .otherwise(true)
                            .as("isLiked"))
                .from(posting)
                .leftJoin(like).on(like.user.id.eq(userId), posting.id.eq(like.posting.id), like.isDeleted.eq(false))
                .where(posting.id.in(postingIds))
                .groupBy(posting.id)
                .fetch();
    }

    public Tuple findLiked(Long postingId, Long userId) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QPosting posting = QPosting.posting;
        QLike like = QLike.like;
        return query
                .select(posting.id,
                        new CaseBuilder()
                                .when(like.id.count().eq(0L))
                                .then(false)
                                .otherwise(true)
                                .as("isLiked"))
                .from(posting)
                .leftJoin(like).on(like.user.id.eq(userId), posting.id.eq(like.posting.id), like.isDeleted.eq(false))
                .where(posting.id.eq(postingId))
                .groupBy(posting.id)
                .fetchOne();
    }


    public PostingDetailDto findOne(Long postingId) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QPosting posting = QPosting.posting;
        QUser user = QUser.user;
        QLike like = QLike.like;

        return query
                .select(new QPostingDetailDto(
                        posting.id,
                        posting.title,
                        posting.content,
                        user.id,
                        posting.createdAt,
                        user.nickname,
                        user.accountType,
                        like.id.count())
                )
                .from(posting)
                .leftJoin(posting.user, user)
                .leftJoin(posting.likes, like)
                .where(posting.id.eq(postingId))
                .groupBy(posting.id)
                .fetchOne();

    }

    public List<PostingsDto> findAll(PostingSearch postingSearch, int offset, int limit) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QPosting posting = QPosting.posting;
        QUser user = QUser.user;
        QLike like = QLike.like;

        return query
                .select(new QPostingsDto(
                        posting.id,
                        posting.title,
                        posting.content,
                        posting.user.id,
                        posting.createdAt,
                        posting.user.nickname,
                        user.accountType,
                        like.id.count())
                )
                .from(posting)
                .leftJoin(posting.user, user)
                .leftJoin(posting.likes, like)
                .where(
                        isTitleContains(postingSearch.getTitle()),
                        isContentContains(postingSearch.getContent())
                )
                .groupBy(posting.id)
                .orderBy(posting.createdAt.desc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    BooleanExpression isTitleContains(String title) {
        if (title == null) {
            return null;
        }

        return QPosting.posting.title.contains(title);
    }

    BooleanExpression isContentContains(String content) {
        if (content == null) {
            return null;
        }
        return QPosting.posting.content.contains(content);
    }
}
