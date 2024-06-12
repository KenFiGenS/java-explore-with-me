package ru.practicum.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitsRepository extends JpaRepository<Hit, Integer>, JpaSpecificationExecutor<Hit> {

    List<Hit> findByTimestampIsAfterAndTimestampIsBefore(LocalDateTime start, LocalDateTime end);
}
