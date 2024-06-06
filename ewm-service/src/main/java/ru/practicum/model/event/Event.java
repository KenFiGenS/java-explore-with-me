package ru.practicum.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.category.Category;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EVENTS")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private int id;
    private String title;
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    private boolean paid;
    @Column(name = "EVENT_DATE")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User initiator;
    private String description;
    @Column(name = "PARTICIPANT_LIMIT")
    private int participantLimit;
    @Enumerated(EnumType.ORDINAL)
    private EventStatus state;
    @Column(name = "CREATED_ON")
    private LocalDateTime createdOn;
    private float lat;
    private float lon;
    private boolean requestModeration;
}
