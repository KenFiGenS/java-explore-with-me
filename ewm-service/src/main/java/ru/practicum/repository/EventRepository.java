package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.event.Event;

public interface EventRepository extends JpaRepository<Event, Integer> {
    Event findByIdAndInitiatorId( int eventId, int userId);

    Page<Event> findByInitiatorId(int userId, Pageable pageable);
}
