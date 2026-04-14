package com.gibanator.dailystepbackendjava.dailycategoryprogress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface DailyCategoryProgressRepository extends JpaRepository<DailyCategoryProgressEntity, DailyCategoryProgressId> {
    @Query("""
            select p
            from DailyCategoryProgressEntity p
            join p.category c
            where p.id.date = :date
            and c.user.id = :userId
           """)
    List<DailyCategoryProgressEntity> findAllByUserAndDate(
            LocalDate date,
            Long userId
    );


    List<DailyCategoryProgressEntity> findAllByIdDateAndIdCategoryIdIn(
            LocalDate date,
            Collection<Long> categoryIds
    );
}
