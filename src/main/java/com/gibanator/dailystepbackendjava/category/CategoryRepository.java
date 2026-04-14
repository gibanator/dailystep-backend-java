package com.gibanator.dailystepbackendjava.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    List<CategoryEntity> findByUserId(Long userId);
    List<CategoryEntity> findAllByIdInAndUserId(Collection<Long> ids, Long userId);
    @Query("""
    select c
    from CategoryEntity c
    where c.user.id = :userId
      and c.isActive = true
    order by c.sortOrder
""")
    List<CategoryEntity> findAllActiveByUser(Long userId);
}
