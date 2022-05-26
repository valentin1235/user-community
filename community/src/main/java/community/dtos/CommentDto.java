package community.dtos;

import community.app.domain.Comment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private Long userId;
    private String displayUserName;
    private LocalDateTime createdAt;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.userId = comment.getUser().getId();
        this.displayUserName = comment.getDisplayName();
        this.createdAt = comment.getCreatedAt();
    }
}