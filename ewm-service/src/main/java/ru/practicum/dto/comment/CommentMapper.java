package ru.practicum.dto.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.model.comment.Comment;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(int userId, int eventId, CommentDtoCreate dtoCreate) {
        return new Comment(
                0,
                userId,
                eventId,
                dtoCreate.getText(),
                LocalDateTime.now()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getEvent(),
                comment.getAuthor(),
                comment.getText(),
                comment.getCreatedOn()
        );
    }
}
