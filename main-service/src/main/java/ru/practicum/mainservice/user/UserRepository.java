package ru.practicum.mainservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.user.dto.UserDto;

import java.util.List;

public interface UserRepository extends JpaRepository<UserDto, Integer> {

    @Query("SELECT new ru.practicum.mainservice.user.dto.UserDto(" +
            "u.id, " +
            "u.email, " +
            "u.name) " +
            "FROM User u " +
            "ORDER BY u.id " +
            "OFFSET :from ROWS " +
            "FETCH FIRST :size ROWS ONLY")
    List<UserDto> findAll(@Param("from") Integer from, @Param("size") Integer size);

    @Query("DELETE FROM User u WHERE u.id = :id")
    @Modifying
    void deleteById(@Param("id") Long id);
}