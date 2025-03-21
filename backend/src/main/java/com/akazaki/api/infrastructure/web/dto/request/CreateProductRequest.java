package com.akazaki.api.infrastructure.web.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateProductRequest {
    @NotBlank(message = "Name is required")
    @Schema(description = "Name", example = "Ramune Raisin")
    private String name;

    @NotBlank(message = "Description is required")
    @Schema(description = "Description", example = "Japanese soda with grape flavor")
    private String description;

    @NotNull(message = "Price is required")
    @Schema(description = "Price", example = "1.99")
    @Min(value = 0, message = "Price must be greater than 0")
    private double price;

    @NotNull(message = "Stock is required")
    @Schema(description = "Stock", example = "100")
    @Min(value = 0, message = "Stock must be greater than 0")
    private int stock;

    @NotNull(message = "Product image is required")
    @Schema(description = "Image", example = "image.jpg")
    private MultipartFile file;

    @NotNull(message = "Category IDs are required")
    @Schema(description = "Category IDs", example = "[1, 2, 3]")
    @Size(min = 1, message = "At least one category must be selected")
    private List<Long> categoryIds;
}