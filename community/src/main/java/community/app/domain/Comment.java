package community.app.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "comments")
@Where(clause = "is_deleted=false")
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_deleted", columnDefinition = "TINYINT", length = 1)
    private boolean isDeleted = false;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postings_id", columnDefinition = "BIGINT")
    private Posting posting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", columnDefinition = "BIGINT")
    private User user;

    // methods
    public static Comment create(String content, Posting posting, User user) {
        Comment comment = new Comment();
        comment.content = content;
        comment.posting = posting;
        comment.user = user;

        return comment;
    }

    public void update(String newContent) {
        this.content = newContent;
    }

    public void remove() {
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Comment)) {
            return false;
        }

        Comment other = (Comment)obj;
        return this.id.equals(other.getId())
                && this.content.equals(other.getContent())
                && this.createdAt.equals(other.getCreatedAt())
                && this.updatedAt.equals(other.getUpdatedAt())
                && this.isDeleted == other.isDeleted();
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() ^ (this.content.hashCode() >>> 32);
    }
}
