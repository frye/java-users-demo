package com.example.usersdemo.user;

import java.util.UUID;

public record User(UUID id, String name, String emoji) {
}
