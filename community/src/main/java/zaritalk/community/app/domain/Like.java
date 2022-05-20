package zaritalk.community.app.domain;

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
@Table(name = "likes")
@Where(clause = "is_deleted=false")
public class Like {

    @Id @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postings_id")
    private Posting posting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public static Like create(Posting posting, User user) {
        Like like = new Like();
        like.posting = posting;
        like.user = user;

        user.getLikes().add(like);
        posting.getLikes().add(like);

        return like;
    }

    public void deactivate() {
        this.user.getLikes().remove(this);
        this.posting.getLikes().remove(this);
        this.isDeleted = true;
    }

    public void activate() {
        this.user.getLikes().add(this);
        this.posting.getLikes().add(this);
        this.isDeleted = false;
    }
}
