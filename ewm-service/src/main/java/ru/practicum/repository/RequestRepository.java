package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer>, JpaSpecificationExecutor<Request> {
    List<Request> findByEvent(int eventId);

    Request findByIdAndRegister(int userId, int requestId);

    List<Request> findByEventAndStatus(int eventId, RequestStatus confirmed);

    List<Request> findByRegister(int userId);
}
