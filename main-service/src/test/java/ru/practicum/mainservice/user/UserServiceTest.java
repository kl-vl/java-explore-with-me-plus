package ru.practicum.mainservice.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.mainservice.user.dto.UserDto;
import ru.practicum.mainservice.user.dto.UserSave;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final UserDto userDto1 = UserDto.builder()
            .id(1L)
            .email("user1@email.com")
            .name("User One")
            .build();

    private final UserDto userDto2 = UserDto.builder()
            .id(2L)
            .email("user2@email.com")
            .name("User Two")
            .build();

    private final UserSave userSave = new UserSave("newuser@email.com", "New User");

    @Test
    void findAllShouldReturnAllUsersWhenIdsIsNull() {
        when(userRepository.findAll(0, 10)).thenReturn(List.of(userDto1, userDto2));

        List<UserDto> result = userService.findAll(0, 10, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1@email.com", result.get(0).getEmail());
        verify(userRepository, times(1)).findAll(0, 10);
        verify(userRepository, never()).findAllById(any());
    }

    @Test
    void findAllShouldReturnAllUsersWhenIdsIsEmpty() {
        when(userRepository.findAll(5, 20)).thenReturn(List.of(userDto1));

        List<UserDto> result = userService.findAll(5, 20, List.of());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll(5, 20);
        verify(userRepository, never()).findAllById(any());
    }

    @Test
    void findAllShouldReturnUsersByIdsWhenIdsProvided() {
        List<Integer> ids = List.of(1, 2);
        when(userRepository.findAllById(ids)).thenReturn(List.of(userDto1, userDto2));

        List<UserDto> result = userService.findAll(0, 10, ids);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("User One", result.get(0).getName());
        verify(userRepository, times(1)).findAllById(ids);
        verify(userRepository, never()).findAll(anyInt(), anyInt());
    }

    @Test
    void findAllShouldReturnEmptyListWhenNoUsersFound() {
        when(userRepository.findAll(0, 10)).thenReturn(List.of());

        List<UserDto> result = userService.findAll(0, 10, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll(0, 10);
    }

    @Test
    void createShouldSaveAndReturnUser() {
        UserDto savedUserDto = UserDto.builder()
                .id(1L)
                .email("newuser@email.com")
                .name("New User")
                .build();

        when(userRepository.save(any(UserDto.class))).thenReturn(savedUserDto);

        UserDto result = userService.create(userSave);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("newuser@email.com", result.getEmail());
        assertEquals("New User", result.getName());

        verify(userRepository, times(1)).save(any(UserDto.class));
    }

    @Test
    void createShouldMapUserSaveToUserDtoCorrectly() {
        UserDto savedUserDto = UserDto.builder()
                .id(1L)
                .email(userSave.getEmail())
                .name(userSave.getName())
                .build();

        when(userRepository.save(any(UserDto.class))).thenReturn(savedUserDto);

        UserDto result = userService.create(userSave);

        assertNotNull(result);
        assertEquals(userSave.getEmail(), result.getEmail());
        assertEquals(userSave.getName(), result.getName());

        verify(userRepository, times(1)).save(argThat(userDto ->
                userSave.getEmail().equals(userDto.getEmail()) &&
                        userSave.getName().equals(userDto.getName())
        ));
    }

    @Test
    void deleteShouldCallRepositoryDelete() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteShouldHandleRepositoryDeleteCalledOnce() {
        Long userId = 5L;

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAllShouldHandlePaginationParametersCorrectly() {
        when(userRepository.findAll(2, 5)).thenReturn(List.of(userDto1));

        List<UserDto> result = userService.findAll(2, 5, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll(2, 5);
    }

    @Test
    void findAllShouldHandleMultipleIdsCorrectly() {
        List<Integer> ids = List.of(1, 3, 5);
        when(userRepository.findAllById(ids)).thenReturn(List.of(userDto1));

        List<UserDto> result = userService.findAll(0, 10, ids);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAllById(ids);
    }
}