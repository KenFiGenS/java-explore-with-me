package ru.practicum.model.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.Name;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "COMMENTS")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private int id;
    @Column(name = "EVENT_ID")
    private int event;
    @Column(name = "USER_ID")
    private int author;
    private String text;
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;
}
