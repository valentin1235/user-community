package community.dto;

import com.querydsl.core.annotations.QueryProjection;
import community.enums.EAccountType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostingDetailDto {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String displayUserName;
    private Long likeCount;
    private LocalDateTime createdAt;
    private boolean isLiked;
    private List<CommentDto> comments;

    @QueryProjection
    public PostingDetailDto(Long postingId, String title, String content, Long userId, LocalDateTime createdAt, String nickname, EAccountType accountType, Long likeCount) {
        this.id = postingId;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;

        this.displayUserName = this.getDisplayName(nickname, accountType);
        this.likeCount = likeCount;
    }

    public String getDisplayName(String nickname, EAccountType accountType) {
        final StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(nickname)
                .append('(')
                .append(accountType.toString())
                .append(')');

        return nameBuilder.toString();
    }
}
