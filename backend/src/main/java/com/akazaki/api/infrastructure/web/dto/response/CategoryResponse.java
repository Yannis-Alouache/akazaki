package com.akazaki.api.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Category response")
public record CategoryResponse (
    @Schema(description = "ID", example = "1")
    Long id,
    @Schema(description = "Name", example = "Soda")
    String name
){}