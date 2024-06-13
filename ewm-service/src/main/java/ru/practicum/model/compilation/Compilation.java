package ru.practicum.model.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.event.Event;

import javax.persistence.*;
import java.util.ArrayList;
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
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "EVENTS_COMPILATIONS",
            joinColumns = {@JoinColumn(name = "COMPILATION_ID")},
            inverseJoinColumns = {@JoinColumn(name = "EVENT_ID")}
    )
    private List<Event> events = new ArrayList<>();
    private boolean pinned;
    private String title;
}
