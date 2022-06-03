package community.dtos;

import com.querydsl.core.annotations.QueryProjection;
import community.enums.EAccountType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostingsDto {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String displayUserName;
    private Long likeCount;
    private LocalDateTime createdAt;
    private boolean isLiked;

    @QueryProjection
    public PostingsDto(Long postingId, String title, String content, Long userId, LocalDateTime createdAt, String userNickname, EAccountType accountType, Long likeCount) {
        this.id = postingId;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;

        this.displayUserName = getDisplayName(userNickname, accountType);
        this.likeCount = likeCount;
    }

    private String getDisplayName(String nickname, EAccountType accountType) {
        final StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(nickname)
                .append('(')
                .append(accountType.toString())
                .append(')');

        return nameBuilder.toString();
    }
}
