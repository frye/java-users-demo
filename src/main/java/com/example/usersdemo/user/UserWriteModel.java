package com.example.usersdemo.user;

import java.util.UUID;

public record UserWriteModel(String name, String emoji, UUID id) {
}
