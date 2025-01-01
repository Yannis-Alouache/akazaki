package com.akazaki.api.domain.ports.in.queries;

public class GetUserQuery {
    private final Long userId;

    public GetUserQuery(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
} 