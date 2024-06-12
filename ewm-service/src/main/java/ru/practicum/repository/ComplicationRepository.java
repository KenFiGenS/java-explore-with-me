package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.compilation.Compilation;

public interface ComplicationRepository extends JpaRepository<Compilation, Integer> {
}
