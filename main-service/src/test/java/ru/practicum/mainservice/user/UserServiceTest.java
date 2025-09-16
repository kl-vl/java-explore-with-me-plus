package ru.practicum.mainservice.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private final User user1 = User.builder()
            .id(1L)
            .email("user1@email.com")
            .name("User One")
            .build();

    private final User user2 = User.builder()
            .id(2L)
            .email("user2@email.com")
            .name("User Two")
            .build();

    private final ru.practicum.mainservice.user.UserDto userDto1 = ru.practicum.mainservice.user.UserDto.builder()
            .id(1L)
            .email("user1@email.com")
            .name("User One")
            .build();

    private final ru.practicum.mainservice.user.UserDto userDto2 = ru.practicum.mainservice.user.UserDto.builder()
            .id(2L)
            .email("user2@email.com")
            .name("User Two")
            .build();

    private final ru.practicum.mainservice.user.UserDto userSaveDto = new ru.practicum.mainservice.user.UserDto(null, "newuser@email.com", "New User");

    @Test
    void findAllShouldReturnAllUsersWhenIdsIsNull() {
        Page<User> userPage = new PageImpl<>(List.of(user1, user2));
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(userRepository.findAllByIds(null, pageable)).thenReturn(userPage);

        List<ru.practicum.mainservice.user.UserDto> result = userService.findAllUsers(0, 10, null);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(userRepository, times(1)).findAllByIds(null, pageable);
        verify(userRepository, never()).findAllById(anyList());
    }

    @Test
    void findAllShouldReturnAllUsersWhenIdsIsEmpty() {
        Page<User> userPage = new PageImpl<>(List.of(user1, user2));
        PageRequest pageable = PageRequest.of(0, 20, Sort.by("id").ascending());

        when(userRepository.findAllByIds(null, pageable)).thenReturn(userPage);

        List<ru.practicum.mainservice.user.UserDto> result = userService.findAllUsers(0, 20, List.of());

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(userRepository, times(1)).findAllByIds(null, pageable);
        verify(userRepository, never()).findAllById(anyList());
    }

    @Test
    void findAllUsersShouldReturnUsersByIdsWhenIdsProvided() {
        List<Long> ids = List.of(1L, 2L);
        Page<User> userPage = new PageImpl<>(List.of(user1, user2));
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(userRepository.findAllByIds(ids, pageable)).thenReturn(userPage);

        List<ru.practicum.mainservice.user.UserDto> result = userService.findAllUsers(0, 10, ids);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(userRepository, times(1)).findAllByIds(ids, pageable);
        verify(userRepository, never()).findAllById(anyList());
    }

    @Test
    void findAllUsersShouldReturnEmptyListWhenNoUsersFound() {
        Page<User> emptyPage = new PageImpl<>(Collections.emptyList());
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(userRepository.findAllByIds(null, pageable)).thenReturn(emptyPage);

        List<ru.practicum.mainservice.user.UserDto> result = userService.findAllUsers(0, 10, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findAllByIds(null, pageable);
    }

    @Test
    void createShouldSaveAndReturnUser() {
        User savedUser = User.builder()
                .id(1L)
                .email("newuser@email.com")
                .name("New User")
                .build();

        ru.practicum.mainservice.user.UserDto expectedDto = ru.practicum.mainservice.user.UserDto.builder()
                .id(1L)
                .email("newuser@email.com")
                .name("New User")
                .build();

        when(userMapper.toEntity(userSaveDto)).thenReturn(savedUser);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expectedDto);

        ru.practicum.mainservice.user.UserDto result = userService.createUser(userSaveDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("newuser@email.com", result.getEmail());
        assertEquals("New User", result.getName());

        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toEntity(userSaveDto);
        verify(userMapper, times(1)).toDto(savedUser);
    }

    @Test
    void createShouldMapUserSaveToUserDtoCorrectly() {
        User userToSave = User.builder()
                .email(userSaveDto.getEmail())
                .name(userSaveDto.getName())
                .build();

        User savedUser = User.builder()
                .id(1L)
                .email(userSaveDto.getEmail())
                .name(userSaveDto.getName())
                .build();

        ru.practicum.mainservice.user.UserDto savedUserDto = ru.practicum.mainservice.user.UserDto.builder()
                .id(1L)
                .email(userSaveDto.getEmail())
                .name(userSaveDto.getName())
                .build();

        when(userMapper.toEntity(userSaveDto)).thenReturn(userToSave);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);

        ru.practicum.mainservice.user.UserDto result = userService.createUser(userSaveDto);

        assertNotNull(result);
        assertEquals(userSaveDto.getEmail(), result.getEmail());
        assertEquals(userSaveDto.getName(), result.getName());

        verify(userMapper, times(1)).toEntity(userSaveDto);
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDto(savedUser);
    }

    @Test
    void deleteShouldCallRepositoryDelete() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteShouldHandleRepositoryDeleteCalledOnce() {
        Long userId = 5L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAllUsersShouldHandlePaginationParametersCorrectly() {
        Page<User> userPage = new PageImpl<>(List.of(user1));
        PageRequest expectedPageable = PageRequest.of(0, 5, Sort.by("id").ascending());

        when(userRepository.findAllByIds(null, expectedPageable)).thenReturn(userPage);

        List<ru.practicum.mainservice.user.UserDto> result = userService.findAllUsers(2, 5, null);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(userRepository, times(1)).findAllByIds(null, expectedPageable);
    }

    @Test
    void findAllUsersShouldHandleMultipleIdsCorrectly() {
        List<Long> ids = List.of(1L, 3L, 5L);
        Page<User> userPage = new PageImpl<>(List.of(user1));
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(userRepository.findAllByIds(ids, pageable)).thenReturn(userPage);

        List<ru.practicum.mainservice.user.UserDto> result = userService.findAllUsers(0, 10, ids);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(userRepository, times(1)).findAllByIds(ids, pageable);
        verify(userRepository, never()).findAllById(anyList());
    }
}