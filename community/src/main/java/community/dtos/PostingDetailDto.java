package community.dtos;

import community.app.controller.PostingController;
import community.app.domain.Comment;
import community.app.domain.Posting;
import community.app.domain.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostingDetailDto {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String displayUserName;
    private int likeCount;
    private LocalDateTime createdAt;
    private boolean isLiked;
    private List<CommentDto> comments = new ArrayList<>();

    public PostingDetailDto(Posting posting, User userOrNull) {
        this.id = posting.getId();
        this.title = posting.getTitle();
        this.content = posting.getContent();
        this.userId = posting.getUser().getId();
        this.createdAt = posting.getCreatedAt();

        this.displayUserName = posting.getDisplayName();
        this.likeCount = posting.getLikes().size();
        this.isLiked = posting.isLikedBy(userOrNull);

        List<Comment> comments = posting.getComments();
        for (Comment comment : comments) {
            this.comments.add(new CommentDto(comment));
        }
    }
}
