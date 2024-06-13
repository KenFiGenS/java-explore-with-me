package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.compilation.Compilation;

import java.util.List;

public interface ComplicationRepository extends JpaRepository<Compilation, Integer> {

    List<Compilation> findAllByPinned(boolean pinned, Pageable pageable);
}
