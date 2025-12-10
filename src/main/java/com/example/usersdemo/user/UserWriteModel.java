package com.example.usersdemo.user;

import java.util.UUID;

/**
 * Write model for create/update requests.
 *
 * Fields are optional to mirror lightweight API semantics.
 * Defaults are applied in {@link UserService} when absent.
 */
public record UserWriteModel(String name, String emoji, UUID id) {}
