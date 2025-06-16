package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Paginated product list response")
public record ProductListResponse(
    @Schema(description = "List of products")
    List<ProductResponse> products,
    
    @Schema(description = "Current page number", example = "0")
    int pageNumber,
    
    @Schema(description = "Number of products per page", example = "10")
    int pageSize,
    
    @Schema(description = "Total number of products", example = "150")
    long totalElements,
    
    @Schema(description = "Total number of pages", example = "15")
    int totalPages,
    
    @Schema(description = "Is this the last page", example = "false")
    boolean last,
    
    @Schema(description = "Is this the first page", example = "true")
    boolean first
) {}
