package zaritalk.community.app.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "comments")
@Where(clause = "is_deleted=false AND parent_comments_id IS NULL")
public class Comment {
    @Id @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "content", length = 65535)
    private String content;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comments_id")
    private Comment parentComment = null;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    private List<Comment> childComments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postings_id")
    private Posting posting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    // methods
    public static Comment create(String content, Posting posting, User user) {
        Comment comment = new Comment();
        comment.content = content;
        comment.posting = posting;
        comment.user = user;

        return comment;
    }

    public Comment reply(String content, Posting posting, User user) {
        Comment childComment = new Comment();
        childComment.content = content;
        childComment.posting = posting;
        childComment.user = user;

        this.childComments.add(childComment);
        childComment.parentComment = this;

        return childComment;
    }

    public void update(String newContent) {
        this.content = newContent;
    }

    public void remove() {
        List<Comment> comments = this.childComments;
        for (Comment comment : comments) {
            comment.isDeleted = true;
        }

        this.isDeleted = true;
    }

    public String getDisplayName() {
        final StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(this.user.getNickname())
                .append('(')
                .append(this.user.getAccountType().toString())
                .append(')');

        return nameBuilder.toString();
    }
}
