package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HITS")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HIT_ID")
    private int id;
    private String app;
    private String uri;
    private String ip;
    @Column(name = "TIME_CREATE")
    private LocalDateTime timestamp;
}
