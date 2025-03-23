package com.akazaki.api.infrastructure.web.dto.response;

import java.util.List;
import com.akazaki.api.domain.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product response")
public record ProductResponse (
        @Schema(description = "ID", example = "1")
        Long id,

        @Schema(description = "Name", example = "Ramune - Goût Raisin Noir 200ml")
        String name,

        @Schema(description = "Description", example = "Bouteille de 200ml de limonade japonaise, au goût de raisin noir")
        String description,

        @Schema(description = "Price", example = "2.40")
        double price,

        @Schema(description = "Stock", example = "36")
        int stock,

        @Schema(description = "URL of the image", example = "/uploads/0eaef134fzeaf.png")
        String imageUrl,

        @Schema(description = "List of the categories")
        List<Category> categories
) {}
