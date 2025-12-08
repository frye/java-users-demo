/**
 * Immutable user record.
 *
 * Fields:
 * - id: unique identifier (UUID)
 * - name: display name
 * - emoji: Unicode emoji rendered in UI
 */
package com.example.usersdemo.user;

import java.util.UUID;

public record User(UUID id, String name, String emoji) {}
}
