package community.app.domain;

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
@Table(name = "postings")
@Where(clause = "is_deleted=false")
public class Posting {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "title", length = 50)
    private String title;

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
    @JoinColumn(name = "users_id", columnDefinition = "BIGINT")
    private User user;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    @OrderBy(value = "createdAt DESC")
    private List<Comment> comments = new ArrayList<>();

    // methods
    public static Posting create(String title, String content, User user) {
        Posting posting = new Posting();
        posting.title = title;
        posting.content = content;
        posting.user = user;

        return posting;
    }

    public void update(String titleOrNull, String contentOrNull) {
        if (titleOrNull != null) {
            this.title = titleOrNull;
        }

        if (contentOrNull != null) {
            this.content = contentOrNull;
        }
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
        if (!(obj instanceof Posting)) {
            return false;
        }

        Posting other = (Posting)obj;
        return this.id.equals(other.getId())
                && this.title.equals(other.getTitle())
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
