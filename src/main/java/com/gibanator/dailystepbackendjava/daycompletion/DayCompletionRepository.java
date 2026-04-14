package com.gibanator.dailystepbackendjava.daycompletion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;


public interface DayCompletionRepository extends JpaRepository<DayCompletionEntity, DayCompletionId> {

    Optional<DayCompletionEntity> findByIdDateAndIdUserId(
            LocalDate date,
            Long userId
    );

    boolean existsByIdDateAndIdUserId(
            LocalDate date,
            Long userId
    );

}
