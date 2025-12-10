package com.example.usersdemo.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link UserService}.
 *
 * These tests verify the in-memory user store operations including
 * findAll, findById, create, and update methods with various input scenarios.
 */
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void findAll_returnsSeededUsersInInsertionOrder() {
        List<User> users = userService.findAll();

        assertEquals(3, users.size(), "Should have exactly 3 seeded users");

        User first = users.get(0);
        assertEquals("Bramble Fright", first.name());
        assertEquals("\uD83D\uDC7B", first.emoji());

        User second = users.get(1);
        assertEquals("Sylvie Scream", second.name());
        assertEquals("\uD83C\uDF83", second.emoji());

        User third = users.get(2);
        assertEquals("Eve Eerie", third.name());
        assertEquals("\uD83E\uDDD9", third.emoji());
    }

    @Test
    void findById_returnsEmptyForMissingId() {
        UUID randomId = UUID.randomUUID();

        Optional<User> result = userService.findById(randomId);

        assertFalse(result.isPresent(), "Should return empty Optional for non-existent user");
    }

    @Test
    void create_generatesIdAndAppliesDefaultsWhenFieldsAbsent() {
        UserWriteModel input = new UserWriteModel(null, null, null);

        User created = userService.create(input);

        assertNotNull(created.id(), "Should generate a non-null ID");
        assertEquals("Anonymous", created.name(), "Should apply default name");
        assertEquals("\uD83D\uDC4B", created.emoji(), "Should apply default emoji");
    }

    @Test
    void create_usesProvidedFieldsAndRetainsProvidedId() {
        UUID providedId = UUID.randomUUID();
        String providedName = "Test User";
        String providedEmoji = "\uD83D\uDE80";
        UserWriteModel input = new UserWriteModel(providedName, providedEmoji, providedId);

        User created = userService.create(input);

        assertEquals(providedId, created.id());
        assertEquals(providedName, created.name());
        assertEquals(providedEmoji, created.emoji());
    }

    @Test
    void create_treatsBlankEmojiAsAbsent() {
        UserWriteModel input = new UserWriteModel("Test User", "", null);

        User created = userService.create(input);

        assertEquals("Test User", created.name());
        assertEquals("\uD83D\uDC4B", created.emoji());
    }

    @Test
    void update_updatesProvidedFieldsAndRetainsExistingForAbsent() {
        User existing = userService.create(new UserWriteModel("Original Name", "\uD83D\uDE00", null));
        UserWriteModel updateInput = new UserWriteModel("Updated Name", null, null);

        User updated = userService.update(existing.id(), updateInput);

        assertEquals(existing.id(), updated.id());
        assertEquals("Updated Name", updated.name());
        assertEquals("\uD83D\uDE00", updated.emoji());
    }

    @Test
    void update_treatsBlankEmojiAsAbsentAndRetainsExisting() {
        User existing = userService.create(new UserWriteModel("Test User", "\uD83D\uDC4D", null));
        UserWriteModel updateInput = new UserWriteModel("Updated Name", "", null);

        User updated = userService.update(existing.id(), updateInput);

        assertEquals(existing.id(), updated.id());
        assertEquals("Updated Name", updated.name());
        assertEquals("\uD83D\uDC4D", updated.emoji());
    }

    @Test
    void update_missingUser_throwsUserNotFoundException() {
        UUID randomId = UUID.randomUUID();
        UserWriteModel updateInput = new UserWriteModel("Some Name", "\uD83D\uDE00", null);

        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.update(randomId, updateInput)
        );

        assertTrue(exception.getMessage().contains(randomId.toString()));
    }
}
