package com.example.usersdemo.web.api;

import com.example.usersdemo.user.User;
import com.example.usersdemo.user.UserNotFoundException;
import com.example.usersdemo.user.UserService;
import com.example.usersdemo.user.UserWriteModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller slice tests for {@link UserRestController}.
 *
 * Tests the REST API endpoints using MockMvc with a mocked UserService.
 * Verifies HTTP status codes, JSON payloads, and error handling.
 */
@WebMvcTest(UserRestController.class)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void getUsers_returns200AndListPayload() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User user1 = new User(id1, "Alice", "\uD83D\uDE00");
        User user2 = new User(id2, "Bob", "\uD83D\uDE03");
        List<User> users = List.of(user1, user2);

        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(id1.toString()))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].emoji").value("\uD83D\uDE00"))
                .andExpect(jsonPath("$[1].id").value(id2.toString()))
                .andExpect(jsonPath("$[1].name").value("Bob"))
                .andExpect(jsonPath("$[1].emoji").value("\uD83D\uDE03"));
    }

    @Test
    void getUser_existing_returns200AndUserPayload() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Charlie", "\uD83D\uDC4D");

        when(userService.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Charlie"))
                .andExpect(jsonPath("$.emoji").value("\uD83D\uDC4D"));
    }

    @Test
    void getUser_missing_returns404() throws Exception {
        UUID userId = UUID.randomUUID();

        when(userService.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void postUser_returns201AndCreatedPayload() throws Exception {
        UUID generatedId = UUID.randomUUID();
        User createdUser = new User(generatedId, "Anonymous", "\uD83D\uDC4B");

        when(userService.create(any(UserWriteModel.class))).thenReturn(createdUser);

        String requestBody = "{}";

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(generatedId.toString()))
                .andExpect(jsonPath("$.name").value("Anonymous"))
                .andExpect(jsonPath("$.emoji").value("\uD83D\uDC4B"));
    }

    @Test
    void putUser_existing_returns200AndUpdatedPayload() throws Exception {
        UUID userId = UUID.randomUUID();
        User updatedUser = new User(userId, "Updated Name", "\uD83C\uDF89");

        when(userService.update(eq(userId), any(UserWriteModel.class))).thenReturn(updatedUser);

        String requestBody = """
                {
                    "name": "Updated Name",
                    "emoji": "\uD83C\uDF89"
                }
                """;

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.emoji").value("\uD83C\uDF89"));
    }

    @Test
    void putUser_missing_returns404() throws Exception {
        UUID userId = UUID.randomUUID();

        when(userService.update(eq(userId), any(UserWriteModel.class)))
                .thenThrow(new UserNotFoundException(userId));

        String requestBody = """
                {
                    "name": "Some Name",
                    "emoji": "\uD83D\uDE00"
                }
                """;

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUser_invalidUuid_returns400() throws Exception {
        mockMvc.perform(get("/api/users/{id}", "not-a-uuid"))
                .andExpect(status().isBadRequest());
    }
}
