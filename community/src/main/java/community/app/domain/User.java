package community.app.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import community.enums.EAccountType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Table(name = "users")
@Where(clause = "quit=false")
public class User {

    @Id @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "account_id", length = 100)
    private String accountId;

    @Column(name = "account_type", columnDefinition = "TINYINT")
    @Convert(converter = AccountTypeConvertor.class)
    private EAccountType accountType;

    @Column(name = "quit", columnDefinition = "TINYINT", length = 1)
    private boolean quit = false;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // relations
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Posting> postings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    // methods
    public static User create(String accountId, String nickname, EAccountType accountType) {
        User user = new User();
        user.accountId = accountId;
        user.nickname = nickname;
        user.accountType = accountType;

        return user;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }

        User other = (User)obj;
        return this.id.equals(other.getId())
                && this.nickname.equals(other.getNickname())
                && this.accountId.equals(other.getAccountId())
                && this.createdAt.equals(other.getCreatedAt())
                && this.updatedAt.equals(other.getUpdatedAt())
                && this.quit == other.isQuit();
    }

    @Override
    public int hashCode() {
        return this.id.hashCode() ^ (this.accountId.hashCode() >>> 32);
    }
}
