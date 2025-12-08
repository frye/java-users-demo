package com.example.usersdemo.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final Map<UUID, User> storage = Collections.synchronizedMap(new LinkedHashMap<>());

    public UserService() {
        seed();
    }

    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public User create(UserWriteModel input) {
        UUID id = input.id() != null ? input.id() : UUID.randomUUID();
        String name = input.name() != null ? input.name() : "Anonymous";
        String emoji = (input.emoji() != null && !input.emoji().isBlank()) ? input.emoji() : "\uD83D\uDC4B";
        User user = new User(id, name, emoji);
        storage.put(id, user);
        return user;
    }

    public User update(UUID id, UserWriteModel input) {
        User existing = storage.get(id);
        if (existing == null) {
            throw new UserNotFoundException(id);
        }
        String name = input.name() != null ? input.name() : existing.name();
        String emoji = (input.emoji() != null && !input.emoji().isBlank()) ? input.emoji() : existing.emoji();
        User updated = new User(id, name, emoji);
        storage.put(id, updated);
        return updated;
    }

    private void seed() {
        List<UserWriteModel> defaults = List.of(
                new UserWriteModel("Bramble Fright", "\uD83D\uDC7B", null),
                new UserWriteModel("Sylvie Scream", "\uD83C\uDF83", null),
                new UserWriteModel("Eve Eerie", "\uD83E\uDDD9", null)
        );
        defaults.forEach(this::create);
    }
}
