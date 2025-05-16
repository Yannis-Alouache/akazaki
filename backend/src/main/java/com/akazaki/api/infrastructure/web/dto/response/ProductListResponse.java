package com.akazaki.api.infrastructure.web.dto.response;

import java.util.List;

public record ProductListResponse(
    List<ProductResponse> products,
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    boolean last,
    boolean first
) {}
