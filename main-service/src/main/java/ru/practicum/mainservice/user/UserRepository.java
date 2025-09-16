package ru.practicum.mainservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.mainservice.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT new ru.practicum.mainservice.user.dto.UserDto(" +
            "u.id, " +
            "u.email, " +
            "u.name) " +
            "FROM User u " +
            "ORDER BY u.id " +
            "OFFSET :from ROWS " +
            "FETCH FIRST :size ROWS ONLY")
    List<UserDto> findAll(@Param("from") Integer from, @Param("size") Integer size);

    @Query("SELECT new ru.practicum.mainservice.user.dto.UserDto(" +
            "u.id," +
            "u.email," +
            "u.name) " +
            "FROM User u WHERE u.id IN :ids")
    List<UserDto> findManyById(List<Integer> ids);

    void deleteById(Long id);

    @Query("SELECT new ru.practicum.mainservice.user.dto.UserDto(" +
            "u.id," +
            "u.email," +
            "u.name) " +
            "FROM User u WHERE u.id = :id")
    Optional<UserDto> findOne(Long id);
}