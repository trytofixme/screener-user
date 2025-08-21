package ru.screener.user.model.domain;

import java.time.Instant;
import java.util.UUID;

public record ProfileDto(UUID id, String username, Instant subscriptionUntil) {}
