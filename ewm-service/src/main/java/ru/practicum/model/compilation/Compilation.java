package ru.practicum.model.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.event.Event;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "COMPILATIONS")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMPILATION_ID")
    private int id;
    @ManyToMany
    @Column(name = "EVENTS_ID")
    private List<Event> events;
    private boolean pinned;
    private String title;
}
