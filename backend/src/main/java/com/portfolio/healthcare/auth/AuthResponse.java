package com.portfolio.healthcare.auth;

import java.time.Instant;

public record AuthResponse(String accessToken, String tokenType, Instant expiresAt) {}