package ru.practicum.mainservice.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE (:ids IS NULL OR u.id IN :ids)")
    Page<User> findAllByIds(@Param("ids") List<Long> ids, Pageable pageable);

    boolean existsByEmail(String email);
}