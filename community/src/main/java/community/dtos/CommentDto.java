package community.dtos;

import com.querydsl.core.annotations.QueryProjection;
import community.enums.EAccountType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private Long userId;
    private String displayUserName;
    private LocalDateTime createdAt;

    @QueryProjection
    public CommentDto(Long id, String content, Long userId, String nickname, EAccountType accountType, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.displayUserName = getDisplayName(nickname, accountType);
        this.createdAt = createdAt;
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