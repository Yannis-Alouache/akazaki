package com.akazaki.api.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Request for paginated product list")
@Data
public class ProductListRequest {
    @Schema(description = "Page number (starts from 0)", example = "0")
    private int page = 0;
    
    @Schema(description = "Number of products per page", example = "10")
    private int size = 10;
}